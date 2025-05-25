package dtos;

    import java.io.Serializable;

    /**
     * Represents a generic request sent to a handler with an action and payload.
     * Can optionally include a user for authentication or context.
     *
     * @author Group 4
     * @version 1.0
     */
    public class Request implements Serializable
    {
        private final String handler;
        private final String action;
        private final Object payload;
        private User user;

        /**
         * Constructs a Request with the specified handler, action, and payload.
         *
         * @param handler the handler to process the request
         * @param action the action to be performed
         * @param payload the data associated with the request
         */
        public Request(String handler, String action, Object payload)
        {
            this.handler = handler;
            this.action = action;
            this.payload = payload;
        }

        /**
         * Constructs a Request with the specified handler, action, payload, and user.
         *
         * @param handler the handler to process the request
         * @param action the action to be performed
         * @param payload the data associated with the request
         * @param user the user associated with the request
         */
        public Request(String handler, String action, Object payload, User user)
        {
            this.handler = handler;
            this.action = action;
            this.payload = payload;
            this.user = user;
        }

        /**
         * Returns the handler for this request.
         *
         * @return the handler string
         */
        public String handler()
        {
            return handler;
        }

        /**
         * Returns the action for this request.
         *
         * @return the action string
         */
        public String action()
        {
            return action;
        }

        /**
         * Returns the payload of this request.
         *
         * @return the payload object
         */
        public Object payload()
        {
            return payload;
        }

        /**
         * Returns the user associated with this request, if any.
         *
         * @return the User object or null if not set
         */
        public User user()
        {
            return user;
        }

        /**
         * Returns a string representation of the request.
         *
         * @return a string describing the request
         */
        public String toString()
        {
            return "Request{" +
                "handler='" + handler + '\'' +
                ", action='" + action + '\'' +
                ", payload=" + payload +
                '}';
        }
    }