package com.walkmates.lab2;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;


import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.walkmates.model.Booking;
import com.walkmates.model.Listing;
import com.walkmates.model.ListingType;
import com.walkmates.model.Provider;
import com.walkmates.model.Seeker;
import com.walkmates.repository.BookingRepository;
import com.walkmates.repository.ListingRepository;
import com.walkmates.repository.ProviderRepository;
import com.walkmates.repository.SeekerRepository;
import com.walkmates.service.BookingService;
import com.walkmates.service.NotificationService;
import com.walkmates.service.PricingCalculator;
import com.walkmates.service.BookingService.BookingRejectedException;

public class BookingServiceTest {

    //uppgift 4.1
    @Test
    @DisplayName("Booking is rejected when NEW seeker already has 1 active booking")
    void bookingAtMaximumLimitIsRejected(){
        SeekerRepository seekers = mock(SeekerRepository.class);
        ListingRepository listings = mock(ListingRepository.class);
        ProviderRepository providers = mock(ProviderRepository.class);
        BookingRepository bookings = mock(BookingRepository.class);
        PricingCalculator pricing = mock(PricingCalculator.class);
        NotificationService notifications = mock(NotificationService.class);
        //BookingService kräver sex dependencies i sin constructor. Detta ger oss kontrollerbara låtsasversioner. 

        BookingService service = new BookingService(seekers, listings, providers, bookings, pricing, notifications);
     
        //Steg - skapa en NEW seeker 
        Seeker seeker = new Seeker(
            "sam@example.com",
            "Sam",
            "0707654321");
            //vi behöver inte sätta TrustTier.NEW eftersom en Seeker från början blir NEW. 

        //Steg - skapa en listing för bokningen
        Listing listing = new Listing(
            "provider-1",
            "Dog walking", 
            "Description",
            ListingType.DOG_WALK);

        Provider provider = new Provider("Provider", 0.0, 0.0);
        
        //Steg - skapa befintlig bokning , dvs "Sam har redan 1 aktiv bokning" vilket ska agera som max
        Booking existingBooking = new Booking(
            "seeker-1",
            "listing-old",
            60);

        //Steg - vad våra mocks ska svara
        when(seekers.findById("seeker-1")).thenReturn(Optional.of(seeker));

        when(listings.findById("listing-1")).thenReturn(Optional.of(listing));

        when(providers.findById("provider-1")).thenReturn(Optional.of(provider));

        when(bookings.findBySeekerId("seeker-1")).thenReturn(List.of(existingBooking));


        BookingRejectedException exception = assertThrows( //jag förväntar mig att något kastas
            BookingRejectedException.class, //exakt den här typen av exception
            () -> service.createBooking("seeker-1", "listing-1", 60)); //när jag försöker skapa den här bokningen
        
            System.out.println(exception.getMessage());
    }

    @Test 
    @DisplayName ("Successful booking sends confirmation notification")
    void successfulBookingSendsConfirmationNotification(){

        SeekerRepository seekers = mock(SeekerRepository.class);
        ListingRepository listings = mock(ListingRepository.class);
        ProviderRepository providers = mock(ProviderRepository.class);
        BookingRepository bookings = mock(BookingRepository.class);
        PricingCalculator pricing = mock(PricingCalculator.class);
        NotificationService notifications = mock(NotificationService.class);
        //BookingService kräver sex dependencies i sin constructor. Detta ger oss kontrollerbara låtsasversioner. 

        BookingService service = new BookingService(seekers, listings, providers, bookings, pricing, notifications);

        //Steg - skapa en NEW seeker 
        Seeker seeker = new Seeker(
            "sam@example.com",
            "Sam",
            "0707654321");
            //vi behöver inte sätta TrustTier.NEW eftersom en Seeker från början blir NEW. 
        seeker.addFunds(100.0); //vi lägger till tillräckligt för att kunna göra betalningen senare.

        //Steg - skapa en listing för bokningen
        Listing listing = new Listing(
            "provider-1",
            "Dog walking", 
            "Description",
            ListingType.DOG_WALK);

            when(seekers.findById("seeker-1")).thenReturn(Optional.of(seeker));
            when(listings.findById("listing-1")).thenReturn(Optional.of(listing));

            // Rule 1: listing must be available.
            // här behöver vi inte göra något, den riktiga koden sköter det åt oss.
            // när vi skapar vår nya listing körs konstruktorn och sätter status Available, 
            //så när denna kolla görs i create booking, är det redan i "rätt läge". 
            // Rule 1 passerar av sig själv.
            // skillnaden är att listing inte är en mock, utan ett riktigt objekt där 
            // konstruktorn körs. Inget behöver mockas om startläget redan passar. 

             // Rule 2: seeker's active bookings below the trust-tier max (FR-4.4 rule 2).
             //här kommer skillnaden - bookings är en mock !
             // den har ingen riktig databas som vet att Sam har 0 bokningar. Därför talar vi om vad den ska svara: 
             when(bookings.findBySeekerId("seeker-1")).thenReturn(List.of());
             // vi instruerar mocken att returnera en tom lista. 
             // vilket ger seekerActive = 0 (tom lista, count zero),
             // och han får ha max en bokning. 0>=1 . Rule 2 passerar. 

             // Rule 3: provider's active bookings below capacity.
        Provider provider = new Provider("Provider", 0.0, 0.0);
            // BookingService behöver själva providern för att fråga om dess capacity. 
            when(providers.findById("provider-1")).thenReturn(Optional.of(provider));
            // Repositories/services/external dependencies → mockar vi.
            // Enkla model-objekt som Seeker, Listing, Provider, Booking → använder vi oftast på riktigt.
            when(listings.findByProviderId("provider-1")).thenReturn(List.of());
            //ger alltså noll listings för denna provider, vilket håller oss under capacity,
            //rule 3 passerar.

             // Rule 4: duration within range — constructed here so the range check (FR-4.1) runs.
             //precis som Rule 1 är detta en check i den riktiga koden, och vi behöver inte göra något här. 

             // Rule 5: total price within the wallet balance.
            //vi har redan satt vår seekers wallet till 100 kr.
            // här behöver vi anävnda any(Booking.class) eftersom att booking skapas inuti createBooking(), 
            // och vi kan inte hänvisa till exakt det objektet, vi har ingen referens.
            when(pricing.priceFor(
                any(Booking.class), 
                any(Listing.class), 
                any(Seeker.class)))
                .thenReturn(50.0);
                // när pricing.priceFor() anropas med en Booking, Listing och Seeker -> låtsas att priset är 50.
                // pris = 50. Vår seekers balance = 100. 
                //Rule 5 passerar. 

            Booking result = service.createBooking("seeker-1", "listing-1", 60);
            //nu kan vi genomföra vår booking 

            // nu viill vi verifiera att vår mock notifications fick ett anrop till sendBookingConfirmed(), 
            // med Sam och bokningen som skapades. 
            verify(notifications).sendBookingConfirmed(seeker, result);
                //Anropades sendBookingConfirmed() faktiskt med just seeker och den booking som createBooking() returnerade?





    }


    
}
