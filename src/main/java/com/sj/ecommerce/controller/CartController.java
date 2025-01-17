package com.sj.ecommerce.controller;


import com.sj.ecommerce.dto.CartDto;
import com.sj.ecommerce.dto.Response;
import com.sj.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // 7. Add Product to Cart
    @PostMapping("/addCart")
    public Response<CartDto> addToCart(@RequestBody CartDto cartDto) {
        return cartService.addToCart(cartDto);
    }

    // 8. Get Cart
    @GetMapping("/getCart/{userId}")
    public Response<CartDto> getCart(@PathVariable String userId) {
        return cartService.getCart(userId);
    }

    // 9. Update Product Quantity in Cart
    @PutMapping("/update/{productId}")
    public Response<CartDto> updateCartProductQuantity(@PathVariable String productId, @RequestBody CartDto request) {
        Response<CartDto> response = new Response<>();
        if (request.getCartItems() == null || request.getCartItems().isEmpty()) {
            response.setMessage("No items found");
            return response;
        }
        Integer quantity = request.getCartItems().getFirst().getQuantity();
        return cartService.updateCartProductQuantity(productId, quantity);
    }

    // 10. Remove Product from Cart
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Response<String>> removeFromCart(@PathVariable String productId, @RequestParam String userId) {
        try {
            // Call the service layer to remove the product from the cart
            Response<String> response = cartService.removeFromCart(userId, productId);
            // Return success response
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return error response
            Response<String> errorResponse = new Response<>();
            errorResponse.setStatus("error");
            errorResponse.setMessage("Failed to remove product from cart: " + e.getMessage());
            errorResponse.setData(null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
