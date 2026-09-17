package com.walkmates.lab1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.walkmates.model.Seeker;
import com.walkmates.model.TrustTier;

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

     @Test
    @DisplayName("Top-up exactly BELOW the 5000 SEK single-transaction maximum is accepted")
    void topUpJustBelowSingleMaximumIsAccepted() {
        Seeker seeker = new Seeker("sam@example.com", "Sam", "0707654321");

        seeker.addFunds(4999.99); // 4999.99, just below the boundary value

        assertThat(seeker.getBalance()).isEqualTo(4999.99);
    }

     @Test
@DisplayName("Top-up just ABOVE the 5000 SEK maximum is rejected")
void topUpJustAboveMaximumIsRejected() {
    Seeker seeker = new Seeker("katten.jansson@gmail.com", "Katten Jansson", "0739587614");

    assertThrows(IllegalArgumentException.class,
                () -> seeker.addFunds(5000.01));
}

@Test
    @DisplayName("Top-up exactly at the 10 SEK single-transaction minimum is accepted")
    void topUpAtSingleMinimumIsAccepted() {
        Seeker seeker = new Seeker("sam@example.com", "Sam", "0707654321");

        seeker.addFunds(Seeker.MIN_TOP_UP); // 10.00, the boundary value

        assertThat(seeker.getBalance()).isEqualTo(Seeker.MIN_TOP_UP);
    }

@Test
    @DisplayName("Top-up just above the 10 SEK single-transaction minimum is accepted")
    void topUpJustAboveSingleMinimumIsAccepted() {
        Seeker seeker = new Seeker("sam@example.com", "Sam", "0707654321");

        seeker.addFunds(10.01);

        assertThat(seeker.getBalance()).isEqualTo(10.01);
    }

    @Test
@DisplayName("Top-up just below the 10 SEK minimum is rejected")
void topUpJustBelowMinimumIsRejected() {
    Seeker seeker = new Seeker("katten.jansson@gmail.com", "Katten Jansson", "0739587614");

    assertThrows(IllegalArgumentException.class,
                () -> seeker.addFunds(9.99));// 9.99, just below the boundary value
}

//below are the wallet balance tests
@Test
@DisplayName("Resulting balance just below 20000 SEK maximum is accepted")
void balanceJustBelowMaximumIsAccepted() {
    Seeker seeker = new Seeker(
        "sam@example.com",
        "Sam",
        "0707654321");

    // Set starting balance to 18000
    seeker.addFunds(5000.00);
    seeker.addFunds(5000.00);
    seeker.addFunds(5000.00);
    seeker.addFunds(3000.00);

    // Just below the 20000 boundary
    seeker.addFunds(1999.99);

    assertThat(seeker.getBalance()).isEqualTo(19999.99);
}

@Test
@DisplayName("Resulting balance at the 20000 SEK maximum is accepted")
void balanceAtMaximumIsAccepted() {
    Seeker seeker = new Seeker(
        "sam@example.com",
        "Sam",
        "0707654321");

    // Set starting balance to 18000
    seeker.addFunds(5000.00);
    seeker.addFunds(5000.00);
    seeker.addFunds(5000.00);
    seeker.addFunds(3000.00);

    // Just at the 20000 boundary
    seeker.addFunds(2000.00);

    assertThat(seeker.getBalance()).isEqualTo(20000.00);
}

@Test
@DisplayName("Resulting balance just above 20000 SEK maximum is rejected")
void balanceJustAboveMaximumIsRejected() {
    Seeker seeker = new Seeker(
        "sam@example.com",
        "Sam",
        "0707654321");

    // Set starting balance to 18000
    seeker.addFunds(5000.00);
    seeker.addFunds(5000.00);
    seeker.addFunds(5000.00);
    seeker.addFunds(3000.00);

    // A top up of 2000.01 would result in 20000.01
    assertThrows(IllegalArgumentException.class,
                () -> seeker.addFunds(2000.01));
}

//below are the EP tests
    @Test
@DisplayName("Valid Swedish phone number is accepted")
void validSwedishPhoneNumberIsAccepted() {
    Seeker seeker = new Seeker(
            "katten.jansson@gmail.com",
            "Katten Jansson",
            "0739587614");

    assertThat(seeker.getPhoneNumber()).isEqualTo("0739587614");
}

    // TODO (EP): one valid + one invalid equivalence class for email, name, and phone (FR-1.1).
    // TODO (BVA): just-below / at / just-above the 10.00 minimum top-up (FR-1.3).
    // TODO (BVA): a top-up that would push the balance above 20000.00 is rejected (FR-1.3).
   

    @Test
    @DisplayName("TODO: replace me — invalid email is rejected at registration")
    void invalidEmailIsRejected() {
        // Example of the shape; expand into your full EP set.
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("not-an-email", "Sam", "0707654321"));
    }

    @Test 
    @DisplayName("Invalid number is rejected at registration")
    void invalidNumberIsRejected(){
        assertThrows(IllegalArgumentException.class, 
            () -> new Seeker("katten.jansson@gmail.com", "Katten Jansson", "072589476"));
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
    @DisplayName("Valid international phone number is accepted")
    void validInternationalNumberIsAccepted(){
         Seeker seeker = new Seeker(
            "katten.jansson@gmail.com",
            "Katten Jansson",
            "+46739587614");

    assertThat(seeker.getPhoneNumber()).isEqualTo("+46739587614");
    }

    @Test
    @DisplayName("Invalid Display name with invalid characters is rejected at registration")
    void invalidDisplayNameIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Seeker("katten@example.com", "Sam123", "0707654321"));
}
 

 // TODO (Decision table): expected fee + max-bookings for each trust tier (FR-1.2).
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
