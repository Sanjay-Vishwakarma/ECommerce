Swagger  - http://localhost:8081/swagger-ui/index.html

//Register User
//Login User
//Get User Profile
//Update User Profile
//Delete User
//Change Password
//Forgot Password

Entity Relationships

    User and Cart
        One User can have one Cart (1:1 relationship).

    User and Order
        One User can place multiple Orders (1:N relationship).

    User and Notification
        One User can receive multiple Notifications (1:N relationship).

    Product and Category
        One Product belongs to one Category (N:1 relationship).
        One Category can have multiple Products (1:N relationship).

    Cart and Product
        A Cart can contain multiple Products with quantities (1:N relationship via embedded CartItem).

    Order and Product
        An Order can include multiple Products with quantities (1:N relationship via embedded OrderItem).

    Order and Payment
        One Order has one Payment (1:1 relationship).

    Product and Inventory
        One Product is associated with one Inventory record (1:1 relationship).

**__________________ Admin Controller __________________**

Handles administrative functions like managing users, categories, products, orders, and more.
APIs:

    User Management:
        GET /admin/users – Get a list of all users.
        GET /admin/users/{userId} – Get details of a specific user.
        DELETE /admin/users/{userId} – Delete a user.
        PUT /admin/users/{userId} – Update user roles or status.

    Category Management:
        POST /admin/categories – Add a new category.
        PUT /admin/categories/{categoryId} – Update an existing category.
        DELETE /admin/categories/{categoryId} – Remove a category.

    Product Management:
        POST /admin/products – Add a new product.
        PUT /admin/products/{productId} – Update product details.
        DELETE /admin/products/{productId} – Delete a product.

    Order Management:
        GET /admin/orders – Get all orders.
        PUT /admin/orders/{orderId} – Update order status (e.g., "Shipped", "Delivered").
        DELETE /admin/orders/{orderId} – Cancel an order.

    Inventory Management:
        POST /admin/inventory – Add inventory for a product.
        PUT /admin/inventory/{productId} – Update inventory stock.
        DELETE /admin/inventory/{productId} – Delete inventory for a product.

    Notification Management:
        POST /admin/notifications – Broadcast a notification to all users.




    /* Durgesh
     @Bean
    public FilterRegistrationBean corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
//        configuration.setAllowedOrigins(Arrays.asList("https://domain2.com","http://localhost:4200"));
configuration.addAllowedOriginPattern("*");
configuration.addAllowedHeader("Authorization");
configuration.addAllowedHeader("Content-Type");
configuration.addAllowedHeader("Accept");
configuration.addAllowedMethod("GET");
configuration.addAllowedMethod("POST");
configuration.addAllowedMethod("DELETE");
configuration.addAllowedMethod("PUT");
configuration.addAllowedMethod("OPTIONS");
configuration.setMaxAge(3600L);
source.registerCorsConfiguration("/**", configuration);

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CorsFilter(source));
        filterRegistrationBean.setOrder(-110);
        return filterRegistrationBean;
    }
     */



/*
http.csrf()
.disable()
.authorizeRequests()
.antMatchers("/auth/login")
.permitAll()
.antMatchers("/auth/google")
.permitAll()
.antMatchers(HttpMethod.POST, "/users")
.permitAll()
.antMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
.antMatchers(PUBLIC_URLS)
.permitAll()
.antMatchers(HttpMethod.GET)
.permitAll()
.anyRequest()
.authenticated()
.and()
.exceptionHandling()
.authenticationEntryPoint(authenticationEntryPoint)
.and()
.sessionManagement()
.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
return http.build();
}
*/