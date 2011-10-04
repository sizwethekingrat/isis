package org.apache.isis.viewer.json.applib;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.ClientRequest;

public final class RestfulRequest {

    public enum DomainModel {
        NONE,
        SIMPLE,
        FORMAL,
        SELECTABLE;
        
        public static Parser<DomainModel> parser() {
            return new Parser<RestfulRequest.DomainModel>() {
                
                @Override
                public DomainModel valueOf(String str) {
                    return DomainModel.valueOf(str.toUpperCase());
                }
                
                @Override
                public String asString(DomainModel t) {
                    return t.name().toLowerCase();
                }
            };
        }
    }
    
    public static class QueryParameter<Q> {

        public static QueryParameter<List<String>> FOLLOW_LINKS = new QueryParameter<List<String>>("x-ro-follow-links", Parser.forListOfStrings());
        public static QueryParameter<Integer> PAGE = new QueryParameter<Integer>("x-ro-page", Parser.forInteger());
        public static QueryParameter<Integer> PAGE_SIZE = new QueryParameter<Integer>("x-ro-page-size", Parser.forInteger());
        public static QueryParameter<List<String>> SORT_BY = new QueryParameter<List<String>>("x-ro-sort-by", Parser.forListOfStrings());
        public static QueryParameter<DomainModel> DOMAIN_MODEL = new QueryParameter<DomainModel>("x-ro-domain-model", DomainModel.parser());
        public static QueryParameter<Boolean> VALIDATE_ONLY = new QueryParameter<Boolean>("x-ro-validate-only", Parser.forBoolean());
        
        private final String name;
        private final Parser<Q> parser;
        
        private QueryParameter(String name, Parser<Q> parser) {
            this.name = name;
            this.parser = parser;
        }
        
        public String getName() {
            return name;
        }

        public Parser<Q> getParser() {
            return parser;
        }

        public Q valueOf(Map<?, ?> parameterMap) {
            @SuppressWarnings("unchecked")
            Map<String, String[]> parameters = (Map<String, String[]>) parameterMap; 
            final String[] values = parameters.get(getName());
            return getParser().valueOf(values);
        }
    }

    public static class Header<X> {
        public static Header<String> IF_MATCH = new Header<String>("If-Match", Parser.forString());
        public static Header<List<String>> ACCEPT = new Header<List<String>>("Accept", Parser.forListOfStrings());
            
        private final String name;
        private final Parser<X> parser;
        private Header(String name, Parser<X> parser) {
            this.name = name;
            this.parser = parser;
        }

        public String getName() {
            return name;
        }
        
        public Parser<X> getParser() {
            return parser;
        }
        
        void setHeader(ClientRequest clientRequest, X t) {
            clientRequest.header(getName(), parser.asString(t));
        }
    }

    private final ClientRequest clientRequest;
    
    public RestfulRequest(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

    public <T> void header(Header<T> header, T t) {
        header.setHeader(clientRequest, t);
    }

    /**
     * Exposed primarily for testing.
     */
    public ClientRequest getClientRequest() {
        return clientRequest;
    }

    public RestfulResponse<JsonRepresentation> execute() {
        try {
            Response executeJaxrs = clientRequest.execute();
            return RestfulResponse.ofT(executeJaxrs);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends JsonRepresentation> RestfulResponse<T> executeT() {
        final RestfulResponse<JsonRepresentation> restfulResponse = execute();
        return (RestfulResponse<T>) restfulResponse;
    }

    public <Q> RestfulRequest withArg(RestfulRequest.QueryParameter<Q> queryParam, String argStr) {
        final Q arg = queryParam.getParser().valueOf(argStr);
        return withArg(queryParam, arg);
    }

    public <Q> RestfulRequest withArg(RestfulRequest.QueryParameter<Q> queryParam, Q arg) {
        return null;
    }

}