/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.alg.preflowpush;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author nima
 */
public class TestPreflowPushPartitioner {
    
    
    public static final List<String> EDGE_LIST = Arrays.asList(new String[] {
        "s -> v2",
        "s -> v1",
        "v9 -> t",
        "v10 -> t",
        "v11 -> t",
        "s -> v0",
        "v7 -> v11",
        "v10 -> v11",
        "v5 -> v8",
        "v7 -> v6",
        "v8 -> v11",
        "v9 -> v10",
        "v3 -> v6",
        "v5 -> v4",
        "v7 -> v10",
        "v1 -> v5",
        "v2 -> v4",
        "v0 -> v1",
        "v4 -> v3",
        "v2 -> v5",
        "v4 -> v7",
        "v1 -> v4",
        "v6 -> v10",
        "v4 -> v6",
        "v6 -> v9",
        "v8 -> v7",
        "v0 -> v4",
        "v4 -> v8",
        "v7 -> v9",
        "v1 -> v2",
        "v0 -> v3",
        "v9 -> v12",
        "v12 -> t",
        "v13 -> t",
        "v8 -> v13"
        });
    
}
