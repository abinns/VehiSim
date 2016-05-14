var Stream = Java.type("java.util.stream.Stream");
function doTick(){
	print("In compiled script!");
	Stream.of(43,346,2,623,6,2361,2,3,345,6345,63,457,3457,345,734,572,5437,345,734,57).sorted().distinct().forEach(print);
}
var tank = {
	thing: function(){
		print("nested")
	}
}