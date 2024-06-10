package com.github.bty834.highlightstream





class MethodCall(methodName: String, val startOffset: Int, val endOffset: Int) {

    var isStreamMethod: Boolean = false;

    init {
        if (streamMethodNameSet.contains(methodName)){
            this.isStreamMethod = true;
        }
    }

    companion object {
        private val streamMethodNameSet: MutableSet<String> = HashSet()

        init {
            streamMethodNameSet.add("map")
            streamMethodNameSet.add("filter")
            streamMethodNameSet.add("collect")
            streamMethodNameSet.add("distinct")
            streamMethodNameSet.add("stream")
            streamMethodNameSet.add("flatMap")
            streamMethodNameSet.add("reduce")
            streamMethodNameSet.add("anyMatch")
            streamMethodNameSet.add("allMatch")
            streamMethodNameSet.add("findFirst")
            streamMethodNameSet.add("findAny")
            streamMethodNameSet.add("findAny")
            streamMethodNameSet.add("peek")
            streamMethodNameSet.add("count")
            streamMethodNameSet.add("count")
            streamMethodNameSet.add("max")
            streamMethodNameSet.add("min")
            streamMethodNameSet.add("skip")
            streamMethodNameSet.add("sorted")
            streamMethodNameSet.add("mapToLong")
            streamMethodNameSet.add("mapToDouble")
            streamMethodNameSet.add("mapToInt")
            streamMethodNameSet.add("flatMapToLong")
            streamMethodNameSet.add("flatMapToDouble")
            streamMethodNameSet.add("flatMapToInt")
            streamMethodNameSet.add("limit")
            streamMethodNameSet.add("noneMatch")
            streamMethodNameSet.add("toArray")
        }

    }
}