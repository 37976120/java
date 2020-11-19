
package com.alibaba.csp.sentinel.dashboard.service;

import java.util.List;
import java.util.Set;

import com.alibaba.csp.sentinel.dashboard.domain.cluster.ClusterAppAssignResultVO;
import com.alibaba.csp.sentinel.dashboard.domain.cluster.request.ClusterAppAssignMap;

/**
 * @author Eric Zhao
 * @since 1.4.1
 */
public interface ClusterAssignService {

    /**
     * Unbind a specific cluster server and its clients.
     *
     * @param app app name
     * @param machineId valid machine ID ({@code host@commandPort})
     * @return assign result
     */
    ClusterAppAssignResultVO unbindClusterServer(String app, String machineId);

    /**
     * Unbind a set of cluster servers and its clients.
     *
     * @param app app name
     * @param machineIdSet set of valid machine ID ({@code host@commandPort})
     * @return assign result
     */
    ClusterAppAssignResultVO unbindClusterServers(String app, Set<String> machineIdSet);

    /**
     * Apply cluster server and client assignment for provided app.
     *
     * @param app app name
     * @param clusterMap cluster assign map (server -> clients)
     * @param remainingSet unassigned set of machine ID
     * @return assign result
     */
    ClusterAppAssignResultVO applyAssignToApp(String app, List<ClusterAppAssignMap> clusterMap,
                                              Set<String> remainingSet);
}
