function token() {
	return tk = t() + t() + t() + t() + t() + t() + t() + t()
};
function t() {
	return Math.floor(65536 * (1 + Math.random())).toString(16).substring(1)
}