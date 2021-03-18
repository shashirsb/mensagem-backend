package pt.min.saude.spms.hos.base.mensagem.backend.impl;

import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import com.lightbend.lagom.javadsl.server.ServerServiceCall;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import pt.min.saude.spms.hos.common.classes.backend.Principal;
import pt.min.saude.spms.hos.common.classes.backend.SystemStamp;
import pt.min.saude.spms.hos.service.utils.backend.ExecutionContext;

public class Stub {

    public static <Request, Response> ServerServiceCall<Request, Response> authorizedStub(List<String> perms, Function1<Principal, ServerServiceCall<Request, Response>> call) {
        return HeaderServiceCall.compose(requestHeader ->
                call.apply(
                        new Principal(
                                "Stub",
                                new SystemStamp(
                                        "Stub",
                                        Option.none()
                                ),
                                ExecutionContext.getAppName()
                        )
                )
        );
    }
}
