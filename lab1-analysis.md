# Part A — Fundamentals (M1)

## Activity 1.1 — Quality-attribute analysis (ISO/IEC 25010)

Here are the three WalkMates features that we picked, and for each feature two ISO/IEC 25010 quality characteristics are presented with a motivation sentence.

> **FR-1.1 Registration**
>
> - A Seeker registers with email, display name, and phone number.
> - Email MUST be unique and match a valid email format (one @, a non-empty local part, and a domain containing at least one .). Max length 254 characters.
> - Display name MUST be 2–40 characters inclusive, letters/spaces/hyphens/apostrophes only.
> - Phone number MUST follow Swedish format 07XXXXXXXX (10 digits, starts 07) or international +467XXXXXXXX.
> - Terms-of-service acceptance is handled outside this teaching model and is not part of the Labs 1–3 test surface.
>
> Email uniqueness is enforced by _SeekerService_; the Seeker > constructor validates the field's format and length.

**ISO/IEC 25010 quality characteristics: 3.4.4 user error protection** capability of a product to prevent operation errors

**Why is this quality characteristic relevant?** Because it is very easy to make mistakes when registering and filling in all the information, and the system should therefor prevent invalid input that could cause problems later like locking someone out from their account, or sending emails/messages to the wrong person.

**ISO/IEC 25010 quality characteristics: 3.1.2 functional correctness** capability of a product to provide accurate results when used by intended users

**Why is this quality characteristic relevant?** When someone is registering it is important that the system only allows correct input according to the specifications.

<br>

> **FR-1.3 Wallet (account balance)**
>
> - A Seeker has a wallet balance in SEK, starting at 0.00.
> - Top-up rules:
>   - Minimum top-up: 10.00 SEK (a top-up of less than 10.00 is rejected).
>   - Maximum single top-up: 5 000.00 SEK.
>   - Maximum resulting balance: 20 000.00 SEK (a top-up that would exceed this is rejected).
> - The balance MUST never go negative. A booking that costs more than the current balance is rejected (see FR-4.3).
> - Amounts are handled to 2 decimal places; round half-up.

**ISO/IEC 25010 quality characteristics: 3.1.2 functional correctness** capability of a product to provide accurate results when used by intended users

**Why is this quality characteristic relevant?** The system must handle top-ups correctly according to the specifications and rules.

**ISO/IEC 25010 quality characteristics: 3.5 reliability** capability of a product to perform specified functions under specified conditions for a specified period of time without interruptions and failures

**Why is this quality characteristic relevant?** The system must handle the wallet balance (rejecting/accepting top ups) correct according to the specified rules.

<br>

> **FR-1.4 Swedish identity (optional verification)**
>
> - A Seeker MAY verify identity with a Swedish personnummer in YYMMDD-NNNN format.
> - The personnummer MUST pass the Luhn checksum on its 10 digits.
> - Successful verification promotes a NEW Seeker to VERIFIED (higher tiers are granted manually by an operator and are out of scope for these labs).

**ISO/IEC 25010 quality characteristics: 3.6.1 confidentiality** capability of a product to ensure that data are accessible only to those authorized to have access

**Why is this quality characteristic relevant?** The system must protect the personal data from unauthorized access.

**ISO/IEC 25010 quality characteristics: 3.6.5 authenticity** capability of a product to prove that the identity of a subject or resource is the one claimed

**Why is this quality characteristic relevant?** The system must verify the personnummer so that it belongs to the actual person.

<br>

> **We chose the FR-1.4 feature and wrote this testable quality requirement;**
>
> > _“To ensure Authenticity, the system must reject 100% of identity verification attempts where the provided personnummer has the correct YYMMDD-NNNN format but fails the Luhn checksum, ensuring the Seeker is not promoted to VERIFIED.”_

<br>

## Activity 1.2 — Bug analysis (error → fault → failure)

### 1. Trace the chain

**Human error:** The programmer could have misunderstood what counts as an active booking and for example is only counting CONFIRMED and IN_PROGRESS as active, while REQUESTED is not. Or be thinking that the only difference between NEW and VERIFIED is the actual verification, not that it changes the amount of possible active bookings at once.

**Fault in the code:** The code could be missing the check (like an if statement or something) that should be preventing a NEW seeker to add another booking if there is already one active.

**Failure in UI:** The fact that the second booking was possible to do is the actual failure that the user saw.

### 2. Find the responsible code

The responsible code is located in the BookingService.java file on line 71. The code line is missing a "=" after the ">", so it should be ">=", indicating that the amount of active bookings for this seeker is "greater than, or equal to" the maximum amount of bookings that is allowed. Currently the code is only blocking another booking once the seeker has reached more than the maximum bookings. It should be blocked once they reach the maximum.

### 3. Which test level should have caught this, which technique?

Since the rule is precise and isolated this should’ve been caught by a unit test. And since this is a comparison mistake, occurring at the edges of ranges, a Boundary Value Analysis (BVA) is the preferred technique.  
_A specification-based test should have caught this since the requirements clearly states the rules and limits for active bookings per tier._

<br>

## Activity 1.3 — Your first test (day-one win)

FIRST_TEST_TUTORIAL.md : Completed and confirmed green.

<br>

# Part B — Specification-based design (M2)

## Activity 2.1 — Equivalence Partitioning

| Partition | Input | Representative | Expected Outcome |
| --------- | ----- | -------------- | ---------------- |
| Test      | Test  | Test           | Test             |
