package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.api.LxdResponse;
import com.cloudbees.lxd.client.api.Operation;
import com.cloudbees.lxd.client.api.ResponseType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Maybe;
import io.reactivex.Single;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Holds a LxdResponse and provide functions to parse its content. Lifecycle of this object is tied to a request to
 * the REST API.
 */
public class LxdResponseParser {
    protected final Call call;
    protected final Response response;
    protected final ObjectMapper mapper;

    public LxdResponseParser(ObjectMapper mapper, Call call, Response response) {
        this.mapper = mapper;
        this.call = call;
        this.response = response;
    }

    public <T> Maybe<T> parseSyncMaybe(TypeReference<LxdResponse<T>> typeReference) {
        LxdResponse<T> response = parse(typeReference, ResponseType.SYNC, 200, 404);
        return response != null ? Maybe.just(response.getData()) : Maybe.empty();
    }

    public <T> Single<T> parseSyncSingle(TypeReference<LxdResponse<T>> typeReference) {
        return Single.just(parse(typeReference, ResponseType.SYNC, 200).getData());
    }

    public LxdResponse<Operation> parseOperation(ResponseType expectedResponseType, int... expectedHttpStatusCodes) {
        return parse(new TypeReference<LxdResponse<Operation>>() {}, expectedResponseType, expectedHttpStatusCodes);
    }

    public <T> LxdResponse<T> parse(TypeReference<LxdResponse<T>> typeReference, ResponseType expectedResponseType, int... expectedHttpStatusCodes) {
        assertHttpResponseCodes(call, response, expectedHttpStatusCodes);
        LxdResponse<T> lxdResponse = null;
        try {
            // we do not use a stream here to get the jsonBody dumped by Jackson when something goes wrong
            String body = response.body().string();
            lxdResponse = mapper.readValue(body, typeReference);
        } catch (IOException e) {
            throw new LxdExceptionBuilder(call.request()).with(response).with(e).build();
        }
        if (lxdResponse.getType() == null || ResponseType.ERROR == lxdResponse.getType()) {
            for(int expectedStatusCode: expectedHttpStatusCodes) {
                if (lxdResponse.getErrorCode() == expectedStatusCode) {
                    return null;
                }
            }
            throw new LxdExceptionBuilder(call.request()).with(lxdResponse).build();
        }
        if (expectedResponseType != null && lxdResponse.getType() != expectedResponseType) {
            throw new LxdExceptionBuilder(call.request()).withMessage(String.format("got bad response type, expected %s got %s", expectedResponseType, lxdResponse.getType())).build();
        }
        return lxdResponse;
    }

    protected void assertHttpResponseCodes(Call call, Response response, int... expectedHttpStatusCodes) {
        int statusCode = response.code();
        if (expectedHttpStatusCodes.length > 0) {
            for (int expected : expectedHttpStatusCodes) {
                if (statusCode == expected) {
                    return;
                }
            }
            throw new LxdExceptionBuilder(call.request()).with(response).build();
        }
    }

    static class LxdExceptionBuilder {
        final StringBuilder sb = new StringBuilder();
        Throwable throwable;

        LxdExceptionBuilder(Request request) {
            init(request);
        }

        void init(Request request) {
            sb.append("Failure executing: ").append(request.method())
                .append(" at: ").append(request.url()).append(".");
        }

        LxdExceptionBuilder with(Response response) {
            sb.append(" Status:").append(response.code()).append(".")
                .append(" Message: ").append(response.message()).append(".");
            try {
                String body = response.body().string();
                sb.append(" Body: ").append(body);
            } catch (Throwable t) {
                sb.append(" Body: <unreadable>");
            }

            return this;
        }

        LxdExceptionBuilder with(LxdResponse lxdResponse) {
            sb.append(" Status:").append(lxdResponse.getErrorCode())
                .append(" Message: ").append(lxdResponse.getError()).append(".");

            return this;
        }

        LxdExceptionBuilder withMessage(String message) {
            sb.append(" Message: ").append(message).append(".");

            return this;
        }

        LxdExceptionBuilder with(Throwable throwable) {
            this.throwable = throwable;

            return this;
        }

        public LxdClientException build() {
            return throwable == null ? new LxdClientException(sb.toString()) : new LxdClientException(sb.toString(), throwable);
        }
    }

    public static class Factory {
        final ObjectMapper mapper;

        public Factory(ObjectMapper mapper) {
            this.mapper = mapper;
        }

        public LxdResponseParser build(Call call, Response response) {
            return new LxdResponseParser(mapper, call, response);
        }
    }
}
