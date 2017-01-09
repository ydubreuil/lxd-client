/*
 * The MIT License
 *
 * Copyright (c) 2017 CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.cloudbees.lxd.client;

import com.cloudbees.lxd.client.utils.HttpUtils;
import com.cloudbees.lxd.client.utils.URLUtils;
import io.reactivex.Completable;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.Buffer;
import okio.ByteString;
import okio.Okio;
import okio.Source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RxWsClientWrapper implements AutoCloseable {
    protected final OkHttpClient client;
    protected final Config config;
    protected final String rootApiUrl;
    ExecutorService executorService = Executors.newCachedThreadPool();

    public RxWsClientWrapper(Config config) {
        this.config = config;
        this.client = HttpUtils.createWsClient(config);
        this.rootApiUrl = URLUtils.join(config.useUnixTransport() ? "http://localhost:80" : config.getBaseURL());
    }

    @Override
    public void close() {
        executorService.shutdown();

        if (client.connectionPool() != null) {
            client.connectionPool().evictAll();
        }
        if (client.dispatcher() != null &&
            client.dispatcher().executorService() != null &&
            !client.dispatcher().executorService().isShutdown()
            ) {
            client.dispatcher().executorService().shutdown();
        }
    }

    private HttpUrl buildResourceUrl(String resourceUrl) {
        return HttpUrl.parse(URLUtils.join(rootApiUrl, resourceUrl));
    }

    public Completable wsCall(String resourceUrl, InputStream in, OutputStream out) {
        Request request = new Request.Builder().get()
            .url(buildResourceUrl(resourceUrl))
            .addHeader("User-Agent", "LXD-Java-Client")
            .build();

        return Completable.create(completableEmitter -> {
            WebSocket ws = client.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    // TODO: Use an Observable to feed the websocket instead of relying on a thread
                    executorService.submit(new InputStreamCopier(in, webSocket));
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    if (in != null)
                        try {
                            in.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    if (out != null)
                        try {
                            out.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    completableEmitter.onError(t);
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    completableEmitter.onComplete();
                }

                @Override
                public void onMessage(WebSocket webSocket, ByteString bytes) {
                    try {
                        if (out != null) {
                            bytes.write(out);
                            out.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    completableEmitter.onComplete();
                    if (in != null)
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            });
        });
    }

    static class InputStreamCopier implements Runnable {
        final InputStream in;
        final WebSocket wsOut;

        public InputStreamCopier(InputStream in, WebSocket wsOut) {
            this.in = in;
            this.wsOut = wsOut;
        }

        @Override
        public void run() {
            try {
                if (in != null) {
                    Source inSrc = Okio.source(in);
                    Buffer sink = new Buffer();
                    while (inSrc.read(sink, 8192) >= 0) {
                        wsOut.send(sink.readByteString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                wsOut.close(1000, "Bye");
            }
        }
    }
}
