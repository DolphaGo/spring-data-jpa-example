package com.example.datajpa.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.datajpa.entity.Item;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @DisplayName("저장")
    @Test
    void save(){
        Item item = new Item("A");
        // UUID 처럼 문자라고 하면, save시에 isNew라고 판단할까? NO. 이미 id를 설정했기 때문에 persist를 호출하지 않음. merge로 감
        itemRepository.save(item); // 그래서 Item Entity에 Persistable<String>를 상속받고, isNew를 Override해서 해결
    }
}