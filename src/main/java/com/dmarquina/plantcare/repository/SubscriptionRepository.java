package com.dmarquina.plantcare.repository;

import com.dmarquina.plantcare.model.Subscription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

}
