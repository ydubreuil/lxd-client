package shared

/*
 * One entry in id mapping set - a single range of either
 * uid or gid mappings.
 */
type IdmapEntry struct {
	Isuid    bool
	Isgid    bool
	Hostid   int // id as seen on the host - i.e. 100000
	Nsid     int // id as seen in the ns - i.e. 0
	Maprange int
}
