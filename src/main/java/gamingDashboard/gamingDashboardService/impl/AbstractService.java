package gamingDashboard.gamingDashboardService.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;


@Component
public class AbstractService {

    public String successWithPayload(Object entity) {
        return Response.ok(entity).build().getEntity().toString();
    }

}
