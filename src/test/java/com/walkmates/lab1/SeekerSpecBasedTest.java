package com.walkmates.lab1;

import com.walkmates.model.Seeker;
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

 


    // TODO (BVA): just-below / at / just-above the 10.00 minimum top-up (FR-1.3).
    // TODO (BVA): a top-up that would push the balance above 20000.00 is rejected (FR-1.3).
    // TODO (Decision table): expected fee + max-bookings for each trust tier (FR-1.2).
    

}
