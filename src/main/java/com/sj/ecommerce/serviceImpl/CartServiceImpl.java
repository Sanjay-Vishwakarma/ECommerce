package com.sj.ecommerce.serviceImpl;

import com.sj.ecommerce.dto.CartDto;
import com.sj.ecommerce.entity.Cart;
import com.sj.ecommerce.helper.Response;
import com.sj.ecommerce.repository.CartRepository;
import com.sj.ecommerce.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Response<CartDto> addToCart(CartDto cartDto) {
        Response<CartDto> response = new Response<>();
        try {
            Cart mapped = modelMapper.map(cartDto, Cart.class);
            Cart cartSaved = cartRepository.save(mapped);
            response.setData(modelMapper.map(cartSaved, CartDto.class));
            response.setMessage("Cart successfully added to the cart");
            response.setStatus("success");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    public Response<CartDto> getCart(String userId) {
        Response<CartDto> response = new Response<>();
        try {
            System.out.println("userId = " + userId);
//            String userId = authenticationService.getAuthenticatedUserId();
            // Fetch the cart for the user
            CartDto byUserIdCartDetails = cartRepository.findByUserId(userId);
            System.out.println("byUserIdCartDetails = " + byUserIdCartDetails);

            // Convert the cart entity to a DTO (Data Transfer Object)
            response.setData(byUserIdCartDetails);
            response.setStatus("success");
            response.setMessage("Cart successfully retrieved from the cart");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    @Override
    public Response<CartDto> updateCartProductQuantity(String productId, Integer quantity) {
        Response<CartDto> response = new Response<>();
        try {
            // Fetch the cart associated with the user (modify as needed for your logic)
            CartDto cartDto = cartRepository.findByProductId(productId);
            System.out.println("cartDto = " + cartDto);
            Cart cart = modelMapper.map(cartDto, Cart.class);

            if (cart == null) {
                response.setStatus("error");
                response.setMessage("Cart or product not found.");
                response.setData(null);
                return response;
            }

            // Update the quantity of the specific product in the cart
            cart.getCartItems().stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst()
                    .ifPresent(item -> item.setQuantity(quantity));

            // Save the updated cart
            Cart updatedCart = cartRepository.save(cart);

            // Map to DTO
            CartDto updatedCartDto = modelMapper.map(updatedCart, CartDto.class);

            response.setStatus("success");
            response.setMessage("Product quantity updated successfully.");
            response.setData(updatedCartDto);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus("error");
            response.setMessage("An error occurred while updating the product quantity.");
            response.setData(null);
        }
        return response;
    }

    @Override
    public Response<String> removeFromCart(String userId, String productId) {
        Response<String> response = new Response<>();
        try {
            // Fetch the cart for the user
            CartDto byUserIdCartDetails = cartRepository.findByUserId(userId);
            Cart cart = modelMapper.map(byUserIdCartDetails, Cart.class);

            if (cart == null) {
                response.setStatus("error");
                response.setMessage("Cart not found for user: " + userId);
                response.setData(null);
                return response;
            }

            // Filter out the product to remove
            List<Cart.CartItem> updatedItems = cart.getCartItems().stream()
                    .filter(item -> !item.getProductId().equals(productId))
                    .collect(Collectors.toList());

            // Update the cart and save
            cart.setCartItems(updatedItems);
            cartRepository.save(cart);

            response.setStatus("success");
            response.setMessage("Product removed from cart.");
            response.setData(productId);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus("error");
            response.setMessage("An error occurred: " + e.getMessage());
            response.setData(null);
        }
        return response;
    }


}
