package com.swiss.wallet.config;

import com.swiss.wallet.entity.Category;
import com.swiss.wallet.entity.Product;
import com.swiss.wallet.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;

@Configuration
public class ProductConfig implements CommandLineRunner {

    private final IProductRepository productRepository;

    private String imageCamisa = "camisa.key";
    private String imageCoca = "coca.key";
    private String imageLivro = "livro.key";

    public ProductConfig(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Product camisa = new Product();
        Product coca = new Product();
        Product livro = new Product();

        ClassPathResource resource = new ClassPathResource(imageCamisa);
        imageCamisa = new String(Files.readAllBytes(resource.getFile().toPath()));
        resource = new ClassPathResource(imageCoca);
        imageCamisa = new String(Files.readAllBytes(resource.getFile().toPath()));
        resource = new ClassPathResource(imageLivro);
        imageCamisa = new String(Files.readAllBytes(resource.getFile().toPath()));

        camisa.setName("Camisa");
        camisa.setValue(40.0F);
        camisa.setDescription("Camisa Preta");
        camisa.setImage(imageCamisa);
        camisa.setCategory(Category.STORE);
        productRepository.save(camisa);

        coca.setName("Coca");
        coca.setValue(10.0F);
        coca.setDescription("Coca Cola");
        coca.setImage(imageCoca);
        coca.setCategory(Category.CANTEEN);
        productRepository.save(coca);

        livro.setName("O Vale dos Mortos");
        livro.setValue(20.0F);
        livro.setDescription("Livro novo");
        livro.setImage(imageLivro);
        livro.setCategory(Category.LIBRARY);
        productRepository.save(livro);

    }
}
