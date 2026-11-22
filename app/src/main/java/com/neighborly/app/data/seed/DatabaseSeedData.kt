package com.neighborly.app.data.seed

import com.neighborly.app.data.local.AppDatabase
import com.neighborly.app.data.local.entity.BookingEntity
import com.neighborly.app.data.local.entity.ProviderEntity
import com.neighborly.app.data.local.entity.ProviderSlotEntity
import com.neighborly.app.data.local.entity.ReviewEntity
import com.neighborly.app.data.local.entity.SubServiceEntity
import com.neighborly.app.data.local.entity.UserAccountEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseSeedData {

    private val timeSlots = listOf(
        "09:00 AM - 10:00 AM",
        "10:00 AM - 11:00 AM",
        "11:00 AM - 12:00 PM",
        "02:00 PM - 03:00 PM",
        "03:00 PM - 04:00 PM",
        "04:00 PM - 05:00 PM",
        "05:00 PM - 06:00 PM",
        "06:00 PM - 07:00 PM"
    )

    private val avatarColors = listOf(
        "#5B5FEF", "#10B981", "#F59E0B", "#8B5CF6",
        "#EC4899", "#06B6D4", "#3B82F6", "#6366F1"
    )

    suspend fun seedDatabaseIfEmpty(db: AppDatabase) = withContext(Dispatchers.IO) {
        val userCount = db.userAccountDao().getUserCount()
        if (userCount == 0) {
            // Seed 2 demo accounts
            val demoUser = UserAccountEntity(
                name = "Vikas Jain",
                phone = "9988776655",
                email = "vikas@neighborly.in",
                password = "password123",
                role = "USER",
                address = "12, Saheli Nagar, Udaipur, RJ",
                providerId = null
            )
            val demoProvider = UserAccountEntity(
                name = "Rajesh Sharma",
                phone = "9829112345",
                email = "rajesh@neighborly.in",
                password = "password123",
                role = "PROVIDER",
                address = "45, Panchwati, Udaipur, RJ",
                providerId = 1L
            )
            db.userAccountDao().insertUser(demoUser)
            db.userAccountDao().insertUser(demoProvider)
        }

        val providerCount = db.providerDao().getProviderCount()
        if (providerCount > 0) return@withContext

        val providers = listOf(
            // Electricians
            ProviderEntity(
                id = 1,
                name = "Rajesh Sharma",
                category = "Electrician",
                initials = "RS",
                avatarColorHex = avatarColors[0],
                rating = 4.8f,
                reviewCount = 84,
                distanceKm = 0.8f,
                responseTimeMins = 15,
                isVerified = true,
                isOnline = true,
                experienceYears = 8,
                phone = "9829112345",
                area = "Panchwati, Udaipur",
                bio = "Certified electrician with 8+ years experience. Specializes in rapid home rewiring, switchboard troubleshooting, and fan/appliance installations.",
                visitingFeeInr = 199
            ),
            ProviderEntity(
                id = 2,
                name = "Amit Verma",
                category = "Electrician",
                initials = "AV",
                avatarColorHex = avatarColors[1],
                rating = 4.6f,
                reviewCount = 62,
                distanceKm = 1.5f,
                responseTimeMins = 20,
                isVerified = true,
                isOnline = true,
                experienceYears = 5,
                phone = "9829223456",
                area = "Hiran Magri, Udaipur",
                bio = "Expert in domestic wiring, smart home light fittings, and inverter backup systems with high safety standards.",
                visitingFeeInr = 149
            ),
            ProviderEntity(
                id = 3,
                name = "Vikram Singh",
                category = "Electrician",
                initials = "VS",
                avatarColorHex = avatarColors[2],
                rating = 4.9f,
                reviewCount = 110,
                distanceKm = 2.2f,
                responseTimeMins = 10,
                isVerified = true,
                isOnline = true,
                experienceYears = 12,
                phone = "9829334567",
                area = "Fatehpura, Udaipur",
                bio = "Senior master electrician. Commercial and residential fault finding, MCB tripping resolution, and 3-phase wiring.",
                visitingFeeInr = 249
            ),
            ProviderEntity(
                id = 4,
                name = "Sunita Devi",
                category = "Electrician",
                initials = "SD",
                avatarColorHex = avatarColors[3],
                rating = 4.7f,
                reviewCount = 45,
                distanceKm = 3.1f,
                responseTimeMins = 25,
                isVerified = true,
                isOnline = false,
                experienceYears = 6,
                phone = "9829445678",
                area = "Sukher, Udaipur",
                bio = "Skilled in solar inverter setups, garden illumination, and household power diagnostic assessments.",
                visitingFeeInr = 199
            ),

            // Plumbers
            ProviderEntity(
                id = 5,
                name = "Ramesh Kumar",
                category = "Plumber",
                initials = "RK",
                avatarColorHex = avatarColors[4],
                rating = 4.9f,
                reviewCount = 128,
                distanceKm = 0.5f,
                responseTimeMins = 12,
                isVerified = true,
                isOnline = true,
                experienceYears = 10,
                phone = "9829556789",
                area = "Delhi Gate, Udaipur",
                bio = "Top-rated plumber in Old City & Central Udaipur. Instant resolution for tap leakages, flush cistern repairs, and drain unblocking.",
                visitingFeeInr = 199
            ),
            ProviderEntity(
                id = 6,
                name = "Manoj Tiwari",
                category = "Plumber",
                initials = "MT",
                avatarColorHex = avatarColors[5],
                rating = 4.5f,
                reviewCount = 38,
                distanceKm = 1.8f,
                responseTimeMins = 30,
                isVerified = true,
                isOnline = true,
                experienceYears = 4,
                phone = "9829667890",
                area = "Surajpole, Udaipur",
                bio = "Budget-friendly, dependable plumbing service for sink replacements, pipe fitting, and shower cartridge servicing.",
                visitingFeeInr = 149
            ),
            ProviderEntity(
                id = 7,
                name = "Deepak Patel",
                category = "Plumber",
                initials = "DP",
                avatarColorHex = avatarColors[6],
                rating = 4.7f,
                reviewCount = 76,
                distanceKm = 2.8f,
                responseTimeMins = 18,
                isVerified = true,
                isOnline = true,
                experienceYears = 7,
                phone = "9829778901",
                area = "Madhuban, Udaipur",
                bio = "Submersible motor specialist, water pressure pump maintenance, and overhead water tank sanitization.",
                visitingFeeInr = 199
            ),
            ProviderEntity(
                id = 8,
                name = "Suresh Yadav",
                category = "Plumber",
                initials = "SY",
                avatarColorHex = avatarColors[7],
                rating = 4.6f,
                reviewCount = 54,
                distanceKm = 4.2f,
                responseTimeMins = 22,
                isVerified = true,
                isOnline = false,
                experienceYears = 9,
                phone = "9829889012",
                area = "Shobhagpura, Udaipur",
                bio = "Complete bathroom remodeling, sanitary ware fixture installs, and geyser pipeline integration.",
                visitingFeeInr = 249
            ),

            // Tutors
            ProviderEntity(
                id = 9,
                name = "Priya Sharma",
                category = "Tutor",
                initials = "PS",
                avatarColorHex = avatarColors[0],
                rating = 4.9f,
                reviewCount = 92,
                distanceKm = 1.1f,
                responseTimeMins = 10,
                isVerified = true,
                isOnline = true,
                experienceYears = 6,
                phone = "9829990123",
                area = "Chetak Circle, Udaipur",
                bio = "M.Sc. in Mathematics. Specializes in conceptual clarity for Class 9–10 CBSE/RBSE Maths & Science. Patient and results-oriented.",
                visitingFeeInr = 350
            ),
            ProviderEntity(
                id = 10,
                name = "Ananya Mukherjee",
                category = "Tutor",
                initials = "AM",
                avatarColorHex = avatarColors[1],
                rating = 4.8f,
                reviewCount = 80,
                distanceKm = 2.5f,
                responseTimeMins = 15,
                isVerified = true,
                isOnline = true,
                experienceYears = 5,
                phone = "9829001234",
                area = "Saheli Nagar, Udaipur",
                bio = "English language trainer & Social Sciences mentor for high schoolers. Focuses on communicative confidence and board exam prep.",
                visitingFeeInr = 400
            ),
            ProviderEntity(
                id = 11,
                name = "Rohan Joshi",
                category = "Tutor",
                initials = "RJ",
                avatarColorHex = avatarColors[2],
                rating = 4.7f,
                reviewCount = 64,
                distanceKm = 3.4f,
                responseTimeMins = 20,
                isVerified = true,
                isOnline = true,
                experienceYears = 8,
                phone = "9829123457",
                area = "Bhuwana, Udaipur",
                bio = "B.Tech graduate teaching Class 11–12 Physics and Foundation JEE/NEET problem-solving methodologies.",
                visitingFeeInr = 500
            ),
            ProviderEntity(
                id = 12,
                name = "Neha Gupta",
                category = "Tutor",
                initials = "NG",
                avatarColorHex = avatarColors[3],
                rating = 5.0f,
                reviewCount = 40,
                distanceKm = 1.9f,
                responseTimeMins = 12,
                isVerified = true,
                isOnline = true,
                experienceYears = 4,
                phone = "9829234568",
                area = "University Road, Udaipur",
                bio = "Elementary & Middle school all-subject educator. Interactive worksheets, Vedic maths shortcuts, and daily study habit coaching.",
                visitingFeeInr = 300
            ),

            // Carpenters
            ProviderEntity(
                id = 13,
                name = "Mohan Suthar",
                category = "Carpenter",
                initials = "MS",
                avatarColorHex = avatarColors[4],
                rating = 4.8f,
                reviewCount = 72,
                distanceKm = 1.2f,
                responseTimeMins = 20,
                isVerified = true,
                isOnline = true,
                experienceYears = 9,
                phone = "9829345679",
                area = "Bapu Bazar, Udaipur",
                bio = "Specialist in modular kitchen drawer repairs, door lock installation, hinge realignments, and custom woodwork.",
                visitingFeeInr = 249
            ),
            ProviderEntity(
                id = 14,
                name = "Kailash Jangid",
                category = "Carpenter",
                initials = "KJ",
                avatarColorHex = avatarColors[5],
                rating = 4.7f,
                reviewCount = 55,
                distanceKm = 2.7f,
                responseTimeMins = 25,
                isVerified = true,
                isOnline = true,
                experienceYears = 7,
                phone = "9829456780",
                area = "Hiran Magri Sector 4, Udaipur",
                bio = "Wardrobe sliding track fix, teakwood polishing, furniture dismantling & reassembly, and custom wooden shelving.",
                visitingFeeInr = 199
            ),

            // AC & Appliance Repair
            ProviderEntity(
                id = 15,
                name = "Imran Khan",
                category = "AC & Appliance",
                initials = "IK",
                avatarColorHex = avatarColors[6],
                rating = 4.9f,
                reviewCount = 115,
                distanceKm = 0.9f,
                responseTimeMins = 15,
                isVerified = true,
                isOnline = true,
                experienceYears = 8,
                phone = "9829567891",
                area = "Surajpole, Udaipur",
                bio = "Certified HVAC technician. High-pressure AC jet pump service, gas leak detection, compressor diagnostics, and fridge cooling fix.",
                visitingFeeInr = 299
            ),
            ProviderEntity(
                id = 16,
                name = "Naresh Gehlot",
                category = "AC & Appliance",
                initials = "NG",
                avatarColorHex = avatarColors[7],
                rating = 4.6f,
                reviewCount = 68,
                distanceKm = 3.0f,
                responseTimeMins = 20,
                isVerified = true,
                isOnline = true,
                experienceYears = 6,
                phone = "9829678902",
                area = "Subhash Nagar, Udaipur",
                bio = "Front & top load washing machine repair, microwave oven magnetron replacement, and water purifier RO filter service.",
                visitingFeeInr = 249
            ),

            // Painters
            ProviderEntity(
                id = 17,
                name = "Laxman Mali",
                category = "Painter",
                initials = "LM",
                avatarColorHex = avatarColors[0],
                rating = 4.8f,
                reviewCount = 84,
                distanceKm = 1.6f,
                responseTimeMins = 15,
                isVerified = true,
                isOnline = true,
                experienceYears = 11,
                phone = "9829789013",
                area = "Fateh Sagar Road, Udaipur",
                bio = "Interior wall putty finishing, royal luxury emulsion texture, anti-dampness waterproofing, and wood lacquer polishing.",
                visitingFeeInr = 199
            ),
            ProviderEntity(
                id = 18,
                name = "Sunil Chouhan",
                category = "Painter",
                initials = "SC",
                avatarColorHex = avatarColors[1],
                rating = 4.7f,
                reviewCount = 49,
                distanceKm = 3.5f,
                responseTimeMins = 30,
                isVerified = true,
                isOnline = true,
                experienceYears = 5,
                phone = "9829890124",
                area = "Pratap Nagar, Udaipur",
                bio = "Budget-friendly whitewash, exterior apex weather-proof coatings, and rust protection enamel for metal gates & grills.",
                visitingFeeInr = 149
            ),

            // Home Cleaning
            ProviderEntity(
                id = 19,
                name = "Radha Bai & Team",
                category = "Home Cleaning",
                initials = "RB",
                avatarColorHex = avatarColors[2],
                rating = 4.9f,
                reviewCount = 130,
                distanceKm = 1.0f,
                responseTimeMins = 12,
                isVerified = true,
                isOnline = true,
                experienceYears = 6,
                phone = "9829901235",
                area = "Madhuban, Udaipur",
                bio = "Complete flat deep cleaning, kitchen oil & chimney degreasing, bathroom calcium tile descaling, and sofa shampooing.",
                visitingFeeInr = 399
            ),
            ProviderEntity(
                id = 20,
                name = "Pooja Cleaning",
                category = "Home Cleaning",
                initials = "PC",
                avatarColorHex = avatarColors[3],
                rating = 4.8f,
                reviewCount = 77,
                distanceKm = 2.3f,
                responseTimeMins = 18,
                isVerified = true,
                isOnline = true,
                experienceYears = 4,
                phone = "9829012346",
                area = "Bedla, Udaipur",
                bio = "Eco-friendly sanitization, glass facade cleaning, terrace scrubbing, and move-in/move-out apartment deep cleaning.",
                visitingFeeInr = 349
            ),

            // Handyman
            ProviderEntity(
                id = 21,
                name = "Balwant Singh",
                category = "Handyman",
                initials = "BS",
                avatarColorHex = avatarColors[4],
                rating = 4.7f,
                reviewCount = 62,
                distanceKm = 1.4f,
                responseTimeMins = 15,
                isVerified = true,
                isOnline = true,
                experienceYears = 10,
                phone = "9829123458",
                area = "Chetak Marg, Udaipur",
                bio = "Your neighborhood all-rounder! TV wall bracket mounting, curtain rod drills, photo frame hanging, and minor home repairs.",
                visitingFeeInr = 199
            ),
            ProviderEntity(
                id = 22,
                name = "Sonu Rao",
                category = "Handyman",
                initials = "SR",
                avatarColorHex = avatarColors[5],
                rating = 4.6f,
                reviewCount = 44,
                distanceKm = 2.9f,
                responseTimeMins = 25,
                isVerified = true,
                isOnline = true,
                experienceYears = 5,
                phone = "9829234569",
                area = "Sector 14 Hiran Magri, Udaipur",
                bio = "IKEA and furniture assembly, door stop & bolt replacements, minor cement patchwork, and odd maintenance jobs.",
                visitingFeeInr = 149
            )
        )

        db.providerDao().insertProviders(providers)

        // Sub-services
        val subServices = listOf(
            // Electricians
            SubServiceEntity(providerId = 1, serviceName = "Switchboard Repair & Socket Replacement", minPriceInr = 150, maxPriceInr = 300),
            SubServiceEntity(providerId = 1, serviceName = "Ceiling Fan Installation & Repair", minPriceInr = 200, maxPriceInr = 450),
            SubServiceEntity(providerId = 1, serviceName = "MCB / Fuse Box Troubleshooting", minPriceInr = 350, maxPriceInr = 800),
            SubServiceEntity(providerId = 2, serviceName = "Inverter & Battery Wiring", minPriceInr = 450, maxPriceInr = 950),
            SubServiceEntity(providerId = 2, serviceName = "Chandelier & Light Fixture Setup", minPriceInr = 250, maxPriceInr = 600),
            SubServiceEntity(providerId = 3, serviceName = "Full Room / House Rewiring", minPriceInr = 1500, maxPriceInr = 4500),
            SubServiceEntity(providerId = 3, serviceName = "3-Phase Commercial Board Checking", minPriceInr = 600, maxPriceInr = 1200),
            SubServiceEntity(providerId = 4, serviceName = "Solar Panel Wiring & Inverter Check", minPriceInr = 800, maxPriceInr = 1800),

            // Plumbers
            SubServiceEntity(providerId = 5, serviceName = "Tap Leakage & Washer Replacement", minPriceInr = 120, maxPriceInr = 250),
            SubServiceEntity(providerId = 5, serviceName = "Flush Tank / Cistern Internal Fitting", minPriceInr = 250, maxPriceInr = 500),
            SubServiceEntity(providerId = 5, serviceName = "Kitchen Sink Drain Unclogging", minPriceInr = 300, maxPriceInr = 600),
            SubServiceEntity(providerId = 6, serviceName = "PVC Pipe Joint & Leak Sealing", minPriceInr = 200, maxPriceInr = 450),
            SubServiceEntity(providerId = 7, serviceName = "Water Motor & Pump Capacitor Fix", minPriceInr = 400, maxPriceInr = 900),
            SubServiceEntity(providerId = 8, serviceName = "Geyser Connection & Pipe Fitting", minPriceInr = 350, maxPriceInr = 750),

            // Tutors
            SubServiceEntity(providerId = 9, serviceName = "Class 9-10 Maths (Weekly 1-on-1)", minPriceInr = 400, maxPriceInr = 600),
            SubServiceEntity(providerId = 9, serviceName = "Class 9-10 Science & Doubt Solving", minPriceInr = 400, maxPriceInr = 600),
            SubServiceEntity(providerId = 10, serviceName = "Conversational English Coaching", minPriceInr = 350, maxPriceInr = 550),
            SubServiceEntity(providerId = 11, serviceName = "Class 11-12 Physics Advanced Mechanics", minPriceInr = 600, maxPriceInr = 950),
            SubServiceEntity(providerId = 12, serviceName = "Primary School All Subjects Support", minPriceInr = 250, maxPriceInr = 450),

            // Carpenters
            SubServiceEntity(providerId = 13, serviceName = "Modular Kitchen Drawer Channel Repair", minPriceInr = 250, maxPriceInr = 600),
            SubServiceEntity(providerId = 13, serviceName = "Main Door Godrej Lock Installation", minPriceInr = 350, maxPriceInr = 750),
            SubServiceEntity(providerId = 14, serviceName = "Wardrobe Sliding Door Track Alignment", minPriceInr = 300, maxPriceInr = 700),
            SubServiceEntity(providerId = 14, serviceName = "Wooden Chair / Table Joint Gluing", minPriceInr = 200, maxPriceInr = 500),

            // AC & Appliance
            SubServiceEntity(providerId = 15, serviceName = "Split AC Foam & Jet Cleaning", minPriceInr = 450, maxPriceInr = 850),
            SubServiceEntity(providerId = 15, serviceName = "AC Gas Top-up & Refrigerant Charging", minPriceInr = 1200, maxPriceInr = 2400),
            SubServiceEntity(providerId = 16, serviceName = "Automatic Washing Machine Spin/Drain Fix", minPriceInr = 350, maxPriceInr = 800),
            SubServiceEntity(providerId = 16, serviceName = "Refrigerator Cooling Coil Diagnosis", minPriceInr = 300, maxPriceInr = 750),

            // Painters
            SubServiceEntity(providerId = 17, serviceName = "Waterproofing & Anti-Damp Wall Treatment", minPriceInr = 600, maxPriceInr = 1800),
            SubServiceEntity(providerId = 17, serviceName = "Single Room Texture / Accent Wall", minPriceInr = 800, maxPriceInr = 2200),
            SubServiceEntity(providerId = 18, serviceName = "Balcony Iron Grill Anti-Rust Paint", minPriceInr = 350, maxPriceInr = 900),

            // Home Cleaning
            SubServiceEntity(providerId = 19, serviceName = "Full 2BHK Intensive Deep Cleaning", minPriceInr = 1499, maxPriceInr = 2799),
            SubServiceEntity(providerId = 19, serviceName = "Kitchen Chimney, Hob & Tiles Degreasing", minPriceInr = 599, maxPriceInr = 1199),
            SubServiceEntity(providerId = 20, serviceName = "Bathroom Acid Wash & Tile Descaling", minPriceInr = 399, maxPriceInr = 799),
            SubServiceEntity(providerId = 20, serviceName = "5-Seater Sofa Foam Shampoo Wash", minPriceInr = 499, maxPriceInr = 999),

            // Handyman
            SubServiceEntity(providerId = 21, serviceName = "LED TV Wall Mount (Up to 55 inch)", minPriceInr = 250, maxPriceInr = 550),
            SubServiceEntity(providerId = 21, serviceName = "Drill & Hang (Curtain Rods, Mirrors, Frames)", minPriceInr = 150, maxPriceInr = 400),
            SubServiceEntity(providerId = 22, serviceName = "Flat-pack Furniture Assembly", minPriceInr = 300, maxPriceInr = 800),
            SubServiceEntity(providerId = 22, serviceName = "Door Latch, Bolt & Stopper Installation", minPriceInr = 150, maxPriceInr = 350)
        )
        db.providerDao().insertSubServices(subServices)

        // Generate 8 Slots for every provider
        val allSlots = mutableListOf<ProviderSlotEntity>()
        for (providerId in 1L..22L) {
            for ((index, slotTime) in timeSlots.withIndex()) {
                val isBlocked = (providerId == 1L && index == 3) || (providerId == 5L && index == 6)
                val isBooked = (providerId == 1L && index == 0) || (providerId == 5L && index == 1)
                allSlots.add(
                    ProviderSlotEntity(
                        providerId = providerId,
                        slotDate = "Today",
                        slotTime = slotTime,
                        isBooked = isBooked,
                        isBlockedByProvider = isBlocked
                    )
                )
            }
        }
        db.slotDao().insertSlots(allSlots)

        // Reviews
        val reviews = listOf(
            ReviewEntity(providerId = 1, customerName = "Sunil Mehta", rating = 5.0f, comment = "Reached within 15 minutes in Panchwati! Cleaned up after fixing the switchboard.", dateStr = "Yesterday"),
            ReviewEntity(providerId = 1, customerName = "Pooja Rathore", rating = 4.5f, comment = "Very polite technician and fair pricing. Ceiling fan runs quietly now.", dateStr = "3 days ago"),
            ReviewEntity(providerId = 5, customerName = "Arjun Rawat", rating = 5.0f, comment = "Excellent work on our old brass tap leakage near Delhi Gate. Highly recommend Ramesh ji.", dateStr = "2 days ago"),
            ReviewEntity(providerId = 9, customerName = "Dr. Mahendra", rating = 5.0f, comment = "Priya ma'am explains mathematical formulas with intuitive visual logic. My son scored 94% in term tests.", dateStr = "1 week ago"),
            ReviewEntity(providerId = 13, customerName = "Gaurav Vyas", rating = 5.0f, comment = "Fixed our kitchen modular hydraulic drawer perfectly. Very courteous.", dateStr = "2 days ago"),
            ReviewEntity(providerId = 15, customerName = "Shailendra K.", rating = 5.0f, comment = "The jet cleaning brought our AC cooling back like brand new. Reached Surajpole promptly.", dateStr = "3 days ago"),
            ReviewEntity(providerId = 19, customerName = "Meenakshi P.", rating = 5.0f, comment = "Radha and her team made our kitchen sparkling clean! Worth every single rupee.", dateStr = "4 days ago"),
            ReviewEntity(providerId = 21, customerName = "Deepak S.", rating = 4.8f, comment = "Balwant mounted our 50-inch TV securely and hung 4 heavy mirrors. Super neat work.", dateStr = "Yesterday")
        )
        db.reviewDao().insertReviews(reviews)

        // Seed initial bookings
        val sampleBookings = listOf(
            BookingEntity(
                id = 1,
                providerId = 1,
                slotId = 1,
                customerName = "Vikas Jain",
                customerPhone = "9988776655",
                subServiceName = "Switchboard Repair & Socket Replacement",
                slotTime = "09:00 AM - 10:00 AM",
                status = "Accepted",
                issueNote = "Sparking in main bedroom 16A AC switchboard. Please check earthing.",
                startOtp = "4829",
                initialQuoteMinInr = 150,
                initialQuoteMaxInr = 300,
                revisedQuoteInr = 350,
                revisedQuoteReason = "Replaced burnt 16A heavy-duty socket & ceramic fuse",
                isRevisedQuoteApproved = false,
                finalPaidAmountInr = null,
                createdAt = System.currentTimeMillis() - 3600000,
                rating = null,
                reviewText = null,
                isReviewed = false
            ),
            BookingEntity(
                id = 2,
                providerId = 5,
                slotId = 34,
                customerName = "Vikas Jain",
                customerPhone = "9988776655",
                subServiceName = "Tap Leakage & Washer Replacement",
                slotTime = "10:00 AM - 11:00 AM",
                status = "Completed",
                issueNote = "Kitchen mixer tap dripping continuously.",
                startOtp = "1942",
                initialQuoteMinInr = 120,
                initialQuoteMaxInr = 250,
                revisedQuoteInr = null,
                revisedQuoteReason = null,
                isRevisedQuoteApproved = false,
                finalPaidAmountInr = 220,
                createdAt = System.currentTimeMillis() - 86400000,
                rating = null,
                reviewText = null,
                isReviewed = false
            )
        )
        for (booking in sampleBookings) {
            db.bookingDao().insertBooking(booking)
        }
    }
}
