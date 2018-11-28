package org.gospel.backend.tools

package object metric {
    def time[R] (block: => R) (label: => String) : R = {
        val t0 = System.currentTimeMillis()
        val result = block    // call-by-name
        val t1 = System.currentTimeMillis()
        println(s"[$label] Elapsed time: ${t1 - t0} ms")
        result
    }
}
