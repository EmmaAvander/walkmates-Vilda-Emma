package com.walkmates.lab1;

import com.walkmates.model.Seeker;
import com.walkmates.model.TrustTier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Lab 1, Part B — specification-based tests for {@link Seeker}.
 *
 * <p>Design your tests on paper first (equivalence partitions, boundary values, decision table)
 * from {@code docs/REQUIREMENTS.md} FR-1.1 / FR-1.3 / FR-1.2, then implement them here. One
 * worked example is provided; the {@code TODO}s are yours.</p>
 */
class SeekerSpecBasedTest {

    // ---- Worked example: boundary value at the maximum single top-up (FR-1.3) ----
    @Test
    @DisplayName("Top-up exactly at the 5000 SEK single-transaction maximum is accepted")
    void topUpAtSingleMaximumIsAccepted() {
        Seeker seeker = new Seeker("sam@example.com", "Sam", "0707654321");

        seeker.addFunds(Seeker.MAX_SINGLE_TOP_UP); // 5000.00, the boundary value

        assertThat(seeker.getBalance()).isEqualTo(Seeker.MAX_SINGLE_TOP_UP);
    }

    // (EP): one valid + one invalid equivalence class for email, name, and phone (FR-1.1).
  @Test
@DisplayName("Valid phone number is accepted")
void validPhoneNumberIsAccepted() {
    Seeker seeker = new Seeker(
            "katten.jansson@gmail.com",
            "Katten Jansson",
            "0739587614");

    assertThat(seeker.getPhoneNumber()).isEqualTo("0739587614");
}

    @Test 
    @DisplayName("Invalid phone number is rejected at registration")
    void invalidPhoneNumberIsRejected(){
        assertThrows(IllegalArgumentException.class, 
            () -> new Seeker("katten.jansson@gmail.com", "Katten Jansson", "072589476"));
    }

  @Test 
    @DisplayName("Valid international phone number is accepted")
    void validInternationalNumberIsAccepted(){
         Seeker seeker = new Seeker(
            "katten.jansson@gmail.com",
            "Katten Jansson",
            "+46739587614");

    assertThat(seeker.getPhoneNumber()).isEqualTo("+46739587614");
    }

      @Test 
    @DisplayName("Invalid international phone number (too long) is rejected at registration")
    void invalidInternationalNumberIsRejected(){
        assertThrows(IllegalArgumentException.class, 
            () -> new Seeker("katten.jansson@gmail.com", "Katten Jansson", "+467258947624"));
    }

      @Test 
    @DisplayName("Valid email is accepted")
    void validEmailIsAccepted(){
         Seeker seeker = new Seeker(
            "katten.jansson@gmail.com",
            "Katten Jansson",
            "0739587614");

    assertThat(seeker.getEmail()).isEqualTo("katten.jansson@gmail.com");
    }

    @Test
    @DisplayName("Invalid email length is rejected at registration")
    void invalidEmailLengthIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("Husmusenaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@example.com", "Sam", "0707654321"));
}

@Test
@DisplayName("Email longer than 254 characters is rejected at registration")
void invalidEmailLengthIsRejectedMethod() {

    String email = "a".repeat(243) + "@example.com";

    assertThrows(IllegalArgumentException.class,
            () -> new Seeker(email, "Sam", "0707654321"));
}

@Test
    @DisplayName("Invalid email format without non-empty local part is rejected at registration")
    void invalidEmailFormatIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("@example.com", "Sam", "0707654321"));
}

    @Test
    @DisplayName("Invalid Display name with invalid characters is rejected at registration")
    void invalidDisplayNameIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("katten@example.com", "Sam123", "0707654321"));
}

@Test
    @DisplayName("Invalid display name (too short) is rejected")
    void invalidNameIsRejected(){
        assertThrows(IllegalArgumentException.class, () -> new Seeker("Katten.jansson@gmail.com", "a", "0739587614"));
    }

    // wallet top-up EP
@Test
    @DisplayName("Valid wallet top-up is accepted")
    void validWalletTopUpIsAccepted(){
        Seeker seeker = new Seeker("Katten.jansson@gmail.com", "katarinaj", "0739587614");
        seeker.addFunds(80.00);
        assertThat(seeker.getBalance()).isEqualTo(80.00);
    }

    @Test
    @DisplayName("Invalid wallet top-up (below minimum) is rejected")
    void invalidWalletTopUpIsRejected(){
        Seeker seeker = new Seeker("Katten.jansson@gmail.com", "katarinaj", "0739587614");
        assertThrows(IllegalArgumentException.class, () -> seeker.addFunds(5.00));
    }

 
    // (BVA): just-below / at / just-above the 10.00 minimum top-up (FR-1.3).

    //Minimum top-up
    @Test
    @DisplayName("Top-up just below minimum (9.99) is rejected")
    void topUpBelowMinimumIsRejected(){
        Seeker seeker = new Seeker("Katten.jansson@gmail.com", "katarinaj", "0739587614");
        assertThrows(IllegalArgumentException.class, () -> seeker.addFunds(9.99));
    }
    @Test
    @DisplayName("Top-up exactly at minimum (10.00) is accepted")
    void topUpAtMinimumIsAccepted(){
        Seeker seeker = new Seeker("Katten.jansson@gmail.com", "katarinaj", "0739587614");
        seeker.addFunds(10.00);
        assertThat(seeker.getBalance()).isEqualTo(10.00);
    }
    @Test
    @DisplayName("Top-up just over minimum (10.00) is accepted")
    void topUpOverMinimumIsAccepted(){
        Seeker seeker = new Seeker("Katten.jansson@gmail.com", "katarinaj", "0739587614");
        seeker.addFunds(10.01);
        assertThat(seeker.getBalance()).isEqualTo(10.01);
    }

     //Single transaction maximum
    @Test
    @DisplayName("Top-up just below single-transaction maximum (4999.99) is accepted")
    void topUpBelowSingleMaxIsAccepted(){
        Seeker seeker = new Seeker("Katten.jansson@gmail.com", "katarinaj", "0739587614");
        seeker.addFunds(4999.99);
        assertThat(seeker.getBalance()).isEqualTo(4999.99);
    }
    //Wanted to try manually input amount instead of the instance variable test higher up
    @Test
    @DisplayName("Top-up exactly at the single-transaction maximum (5000.00) is accepted")
    void topUpExactlyAtSingleMaxIsAccepted(){
        Seeker seeker = new Seeker("Katten.jansson@gmail.com", "katarinaj", "0739587614");
        seeker.addFunds(5000.00);
        assertThat(seeker.getBalance()).isEqualTo(5000.00);
    }
    @Test
    @DisplayName("Top-up just above the single-transaction maximum (5000.01) is rejected")
    void topUpJustAboveSingleMaxIsRejected(){
        Seeker seeker = new Seeker("Katten.jansson@gmail.com", "katarinaj", "0739587614");
        assertThrows(IllegalArgumentException.class, () -> seeker.addFunds(5000.01));
    }

     //Maximum balance
    @Test
    @DisplayName("Top-up just below maximum amount balance is accepted")
    void topUpJustBelowMaxAmountBalanceIsAccepted(){
        Seeker seeker = new Seeker("Katten.jansson@gmail.com", "katarinaj", "0739587614");
        for(int i = 0; i < 3; i++){
            seeker.addFunds(5000.00);
        }
        seeker.addFunds(4999.99);
        assertThat(seeker.getBalance()).isEqualTo(19999.99);
    }
    @Test
    @DisplayName("Top-up at maximum amount balance is accepted")
    void topUpAtMaxAmountBalanceIsAccepted(){
        Seeker seeker = new Seeker("Katten.jansson@gmail.com", "katarinaj", "0739587614");
        for(int i = 0; i < 4; i++){
            seeker.addFunds(5000.00);
        }
       
        assertThat(seeker.getBalance()).isEqualTo(20000.00);
    }

    // (BVA): a top-up that would push the balance above 20000.00 is rejected (FR-1.3).

    @Test
    @DisplayName("Top-up just above maximum amount balance is rejected")
    void topUpJustAboveMaxAmountBalanceIsRejected(){
        Seeker seeker = new Seeker("Katten.jansson@gmail.com", "katarinaj", "0739587614");
        for(int i = 0; i < 3; i++){
            seeker.addFunds(5000.00);
        }
        seeker.addFunds(4991.00);
        assertThrows(IllegalArgumentException.class, () -> seeker.addFunds(10.01));
        //Making sure the test throws an exception due to not allowed max balance, and not a faulty top-up (less than 10.00kr)
    }

    // Decision table: expected fee + max-bookings for each trust tier (FR-1.2).
    
    @Test
@DisplayName("NEW trust tier has max 1 booking and 15% platform fee")
void newTierHasCorrectLimits() {
    Seeker seeker = new Seeker(
        "sam@example.com",
        "Sam",
        "0707654321");

    assertThat(seeker.getMaxConcurrentBookings()).isEqualTo(1);
    assertThat(seeker.getTrustTier().getPlatformFee()).isEqualTo(0.15);
}

 @Test
@DisplayName("VERIFIED trust tier has max 3 bookings and 12% platform fee")
void verifiedTierHasCorrectLimits() {
    Seeker seeker = new Seeker(
        "sam@example.com",
        "Sam",
        "0707654321");

        seeker.setTrustTier(TrustTier.VERIFIED);

    assertThat(seeker.getMaxConcurrentBookings()).isEqualTo(3);
    assertThat(seeker.getTrustTier().getPlatformFee()).isEqualTo(0.12);
}

 @Test
@DisplayName("TRUSTED trust tier has max 5 bookings and 8% platform fee")
void trustedTierHasCorrectLimits() {
    Seeker seeker = new Seeker(
        "sam@example.com",
        "Sam",
        "0707654321");

        seeker.setTrustTier(TrustTier.TRUSTED);

    assertThat(seeker.getMaxConcurrentBookings()).isEqualTo(5);
    assertThat(seeker.getTrustTier().getPlatformFee()).isEqualTo(0.08);
}

 @Test
@DisplayName("PRO_SITTER trust tier has max 10 bookings and 5% platform fee")
void proSitterTierHasCorrectLimits() {
    Seeker seeker = new Seeker(
        "sam@example.com",
        "Sam",
        "0707654321");

        seeker.setTrustTier(TrustTier.PRO_SITTER);

    assertThat(seeker.getMaxConcurrentBookings()).isEqualTo(10);
    assertThat(seeker.getTrustTier().getPlatformFee()).isEqualTo(0.05);
}


}
