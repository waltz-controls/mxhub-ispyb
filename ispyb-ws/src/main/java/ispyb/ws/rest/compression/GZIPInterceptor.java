package ispyb.ws.rest.compression;

import org.apache.cxf.interceptor.AbstractOutDatabindingInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;

import java.lang.reflect.Method;

public class GZIPInterceptor extends AbstractOutDatabindingInterceptor {

    private GZIPOutInterceptor gzipOutInterceptor;

    public GZIPInterceptor() {
        super(Phase.MARSHAL);
        gzipOutInterceptor = new GZIPOutInterceptor();
    }

    public void handleMessage(Message message) {
        Object operation = message.getExchange().get("org.apache.cxf.resource.operation");
        if (operation instanceof java.lang.reflect.Method) {
            Method method = (Method) operation;
            if (method.isAnnotationPresent(GZIP.class) || method.getDeclaringClass().isAnnotationPresent(GZIP.class)) {
                gzipOutInterceptor.handleMessage(message);
            }
        }
    }
}
