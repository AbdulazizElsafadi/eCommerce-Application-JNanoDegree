package com.example.demo;


import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest(classes = SareetaApplication.class)
public class ItemTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItemById() {
        Item item = new Item();
        item.setId(1L);
        item.setName("product1");
        item.setPrice(new BigDecimal(1));
        item.setDescription("desc1");

        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Long.valueOf(1), Objects.requireNonNull(response.getBody()).getId());
    }

    @Test
    public void getItemsByName() {

        Item item = new Item();
        item.setId(1L);
        item.setName("product1");
        item.setPrice(new BigDecimal(1));
        item.setDescription("desc1");

        when(itemRepository.findByName("product1")).thenReturn(Collections.singletonList(item));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("product1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("product1", response.getBody().get(0).getName());
    }

    @Test
    public void getItems() {
        Item item = new Item();
        item.setId(1L);
        item.setName("product1");
        item.setPrice(new BigDecimal(1));
        item.setDescription("desc1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("product2");
        item2.setPrice(new BigDecimal(2));
        item2.setDescription("desc2");

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item, item2));

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Long.valueOf(2), response.getBody().get(1).getId());
    }
}
