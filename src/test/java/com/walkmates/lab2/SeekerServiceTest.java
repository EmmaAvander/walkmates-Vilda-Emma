package com.walkmates.lab2;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.walkmates.model.Seeker;
import com.walkmates.repository.SeekerRepository;
import com.walkmates.service.NotificationService;
import com.walkmates.service.PaymentService;
import com.walkmates.service.SeekerService;


public class SeekerServiceTest {
    
@Test 
@DisplayName ("Successful top up credits the seeker wallet")
void successfulTopUpCreditsWallet() throws PaymentService.PaymentException {

    //SeekerService kräver tre dependencies i sin constructor. 
    SeekerRepository seekers = mock(SeekerRepository.class);
    PaymentService payments = mock(PaymentService.class);
    NotificationService notifications = mock(NotificationService.class);
    
    SeekerService service = new SeekerService(seekers, payments, notifications);

    //skapar vår seeker 
    Seeker seeker = new Seeker(
            "sam@example.com",
            "Sam",
            "0707654321");

    //lär mocken att när SeekerService frågar vår mock seekers, efter seeker-1, ge tillbaka objektet seeker. 
    when(seekers.findById("seeker-1")).thenReturn(Optional.of(seeker));
    
    when(seekers.save(seeker)).thenReturn(seeker);

    Seeker result = service.topUp("seeker-1", "test-1", 50.0);
    assertThat(result.getBalance()).isEqualTo(50.0);
}

@Test
@DisplayName ("Declined payment does not add money to the seeker wallet")
void declinedPaymentDoesNotAddMoneyToWallet()
    throws PaymentService.PaymentException {

    SeekerRepository seekers = mock(SeekerRepository.class);
    PaymentService payments = mock(PaymentService.class);
    NotificationService notifications = mock(NotificationService.class);

    SeekerService service = new SeekerService(seekers, payments, notifications);

    //skapar vår seeker 
    Seeker seeker = new Seeker("sam@gmail.com", "Sam", "0736541239");

    when(seekers.findById("seeker-1")).thenReturn(Optional.of(seeker));

    //nästa del för decline är payments.charge 
    //som vi vill ska kasta PaymentException
    //i success-versionen behövde vi inte göra något, eftersom metoden är void, den typ bara "kontaktar banken" t.ex. 
    //men nu simulrerar vi att betalningen nekades. 
//för void metoder använder Mockito istället mönstret: 
// doThrow(exception)
//      .when(mock)
//      .metod(argument); 
//vi vill säga "kasta payment declined", när vår payments-mock får order att charge "seeker-1", "test-1", och 50.0.
    doThrow(new PaymentService.PaymentException("Payment declined")) //tittade i PaymentService för att se vad som krävdes för PaymentException
        .when(payments)
        .charge("seeker-1", "test-1", 50.0);
        // ARRANGE. Om betalningen på 50 försöks -> neka den. 

// nu vill vi säga att vi förväntar oss att just detta anrop kastar PaymentException. 
    assertThrows(PaymentService.PaymentException.class, 
        () -> service.topUp("seeker-1", "test-1", 50.0));
        // ACT + ASSERT: Gör top-up -> jag förväntar mig PaymentException.

    assertThat(seeker.getBalance()).isEqualTo(0.0);
        // ASSERT the wallet i snot credited when the charge fails. 

}

@Test 
@DisplayName ("Payment timeout does not add money to the seeker wallet")
void paymentTimeoutDoesNotAddMoneyToWallet()
    throws PaymentService.PaymentException {

        SeekerRepository seekers = mock(SeekerRepository.class);
        PaymentService payments = mock(PaymentService.class);
        NotificationService notifications = mock(NotificationService.class);

        SeekerService service = new SeekerService(seekers, payments, notifications);

        Seeker seeker = new Seeker(
            "sam@gmail.com", 
            "Sam", 
            "0735687412");
        
        when(seekers.findById("seeker-1")).thenReturn(Optional.of(seeker));

    doThrow(new PaymentService.PaymentTimeoutException("Payment Timeout"))
        .when(payments)
        .charge("seeker-1", 
        "test-1", 
        50.0);

    assertThrows(PaymentService.PaymentTimeoutException.class,
        () -> service.topUp("seeker-1", "test-1", 50.0));

    assertThat(seeker.getBalance()).isEqualTo(0.0);


}



}
