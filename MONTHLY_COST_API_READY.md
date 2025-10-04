# Monthly Cost API - Ready to Use! ✅

## 🎉 Implementation Complete

All CRUD operations for monthly cost management are now working!

---

## 📋 API Endpoints

### Base URL
`http://localhost:8080/api/v1/monthly-costs`

---

## ✅ 1. CREATE - Add Monthly Cost Configuration

**Endpoint:** `POST /api/v1/monthly-costs`

**Request Body:**
```json
{
  "marketId": 1,
  "year": 2025,
  "month": 11,
  "totalBillingSqft": 15000.00,
  "useAutoCalculation": false,
  "electricityRate": 15.20,
  "acEnabled": true,
  "totalAcCost": 85000.00,
  "serviceEnabled": true,
  "totalServiceCost": 180000.00,
  "generatorEnabled": true,
  "totalGeneratorCost": 6300.00,
  "specialEnabled": false,
  "totalSpecialCost": 0.00,
  "specialCostName": "",
  "specialCostRemarks": "",
  "issueDate": "2025-12-05",
  "dueDate": "2025-12-25"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "market": {
    "id": 1,
    "name": "Default Market",
    ...
  },
  "year": 2025,
  "month": 11,
  "monthName": "November",
  "period": "2025-11-01",
  "totalBillingSqft": 15000.0,
  "useAutoCalculation": false,
  "electricityRate": 15.2,
  "acEnabled": true,
  "totalAcCost": 85000.0,
  "acRatePerSqft": 5.67,          ← Auto-calculated!
  "serviceEnabled": true,
  "totalServiceCost": 180000.0,
  "serviceRatePerSqft": 12.0,     ← Auto-calculated!
  "generatorEnabled": true,
  "totalGeneratorCost": 6300.0,
  "generatorRatePerSqft": 0.42,   ← Auto-calculated!
  "specialEnabled": false,
  "totalSpecialCost": 0.0,
  "specialRatePerSqft": 0.0,
  "specialCostName": "",
  "specialCostRemarks": "",
  "issueDate": "2025-12-05",
  "dueDate": "2025-12-25",
  "locked": false,
  "createdAt": "2025-10-04T12:02:07Z",
  "updatedAt": "2025-10-04T12:02:07Z"
}
```

**Test Command:**
```bash
curl -X POST http://localhost:8080/api/v1/monthly-costs \
  -H 'Content-Type: application/json' \
  -d '{
    "marketId": 1,
    "year": 2025,
    "month": 11,
    "totalBillingSqft": 15000.00,
    "useAutoCalculation": false,
    "electricityRate": 15.20,
    "acEnabled": true,
    "totalAcCost": 85000.00,
    "serviceEnabled": true,
    "totalServiceCost": 180000.00,
    "generatorEnabled": true,
    "totalGeneratorCost": 6300.00,
    "specialEnabled": false,
    "totalSpecialCost": 0.00,
    "issueDate": "2025-12-05",
    "dueDate": "2025-12-25"
  }'
```

---

## ✅ 2. GET LIST - Retrieve Monthly Costs

### Get All for Market
**Endpoint:** `GET /api/v1/monthly-costs?marketId=1`

### Get Specific Month
**Endpoint:** `GET /api/v1/monthly-costs?marketId=1&year=2025&month=11`

### Get All for Year
**Endpoint:** `GET /api/v1/monthly-costs?marketId=1&year=2025`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "market": {...},
    "year": 2025,
    "month": 11,
    "monthName": "November",
    "period": "2025-11-01",
    "totalBillingSqft": 15000.0,
    "electricityRate": 15.2,
    "acRatePerSqft": 5.67,
    "serviceRatePerSqft": 12.0,
    "generatorRatePerSqft": 0.42,
    ...
  }
]
```

**Test Commands:**
```bash
# Get all
curl http://localhost:8080/api/v1/monthly-costs?marketId=1

# Get specific month
curl 'http://localhost:8080/api/v1/monthly-costs?marketId=1&year=2025&month=11'

# Get all for 2025
curl 'http://localhost:8080/api/v1/monthly-costs?marketId=1&year=2025'
```

---

## ✅ 3. GET BY ID - Single Record

**Endpoint:** `GET /api/v1/monthly-costs/{id}`

**Response (200 OK):**
```json
{
  "id": 1,
  "market": {...},
  "year": 2025,
  "month": 11,
  "monthName": "November",
  ...
}
```

**Test Command:**
```bash
curl http://localhost:8080/api/v1/monthly-costs/1
```

---

## ✅ 4. UPDATE - Modify Configuration

**Endpoint:** `PUT /api/v1/monthly-costs/{id}`

**Request Body:**
```json
{
  "totalBillingSqft": 15500.00,
  "useAutoCalculation": false,
  "electricityRate": 16.00,
  "acEnabled": true,
  "totalAcCost": 90000.00,
  "serviceEnabled": true,
  "totalServiceCost": 185000.00,
  "generatorEnabled": true,
  "totalGeneratorCost": 7000.00,
  "specialEnabled": true,
  "totalSpecialCost": 15000.00,
  "specialCostName": "Building Maintenance",
  "specialCostRemarks": "Elevator repair and painting",
  "issueDate": "2025-12-05",
  "dueDate": "2025-12-30"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "acRatePerSqft": 5.81,          ← Recalculated!
  "serviceRatePerSqft": 11.94,    ← Recalculated!
  "generatorRatePerSqft": 0.45,   ← Recalculated!
  "specialRatePerSqft": 0.97,     ← Recalculated!
  "specialCostName": "Building Maintenance",
  ...
}
```

**Test Command:**
```bash
curl -X PUT http://localhost:8080/api/v1/monthly-costs/1 \
  -H 'Content-Type: application/json' \
  -d '{
    "totalBillingSqft": 15500.00,
    "useAutoCalculation": false,
    "electricityRate": 16.00,
    "acEnabled": true,
    "totalAcCost": 90000.00,
    "serviceEnabled": true,
    "totalServiceCost": 185000.00,
    "generatorEnabled": true,
    "totalGeneratorCost": 7000.00,
    "specialEnabled": true,
    "totalSpecialCost": 15000.00,
    "specialCostName": "Building Maintenance",
    "specialCostRemarks": "Elevator repair",
    "issueDate": "2025-12-05",
    "dueDate": "2025-12-30"
  }'
```

---

## ✅ 5. DELETE - Remove Configuration

**Endpoint:** `DELETE /api/v1/monthly-costs/{id}`

**Response (204 No Content):**
```
(Empty body)
```

**Error Response (if locked):**
```json
{
  "status": 500,
  "message": "Cannot delete locked monthly cost configuration. Unlock it first."
}
```

**Test Command:**
```bash
curl -X DELETE http://localhost:8080/api/v1/monthly-costs/1
```

---

## ✅ 6. LOCK - Prevent Changes

**Endpoint:** `POST /api/v1/monthly-costs/{id}/lock`

**Response (200 OK):**
```json
{
  "id": 1,
  "locked": true,
  "message": "Monthly cost configuration locked successfully"
}
```

**Test Command:**
```bash
curl -X POST http://localhost:8080/api/v1/monthly-costs/1/lock
```

---

## ✅ 7. UNLOCK - Allow Changes

**Endpoint:** `POST /api/v1/monthly-costs/{id}/unlock`

**Response (200 OK):**
```json
{
  "id": 1,
  "locked": false,
  "message": "Monthly cost configuration unlocked successfully"
}
```

**Test Command:**
```bash
curl -X POST http://localhost:8080/api/v1/monthly-costs/1/unlock
```

---

## 📊 Database Table Created

Table: `monthly_costs`

**Columns:**
- `id` - Primary key
- `market_id` - Foreign key to markets
- `year` - Year (2025)
- `month` - Month (1-12)
- `period` - Auto-generated date (first day of month)
- `total_billing_sqft` - Total shop area
- `use_auto_calculation` - Auto-calculate sqft from shops
- `electricity_rate` - Rate per kwh
- `ac_enabled`, `total_ac_cost`, `ac_rate_per_sqft`
- `service_enabled`, `total_service_cost`, `service_rate_per_sqft`
- `generator_enabled`, `total_generator_cost`, `generator_rate_per_sqft`
- `special_enabled`, `total_special_cost`, `special_rate_per_sqft`
- `special_cost_name`, `special_cost_remarks`
- `issue_date`, `due_date`
- `locked`
- `created_at`, `updated_at`

**Indexes:**
- `idx_monthly_costs_market_id`
- `idx_monthly_costs_period`
- `idx_monthly_costs_locked`

**Unique Constraint:**
- Market + Year + Month (no duplicates)

---

## 🔑 Key Features

### ✅ Auto-Calculation
- All rates per sqft are automatically calculated from total costs
- Formula: Rate = Total Cost ÷ Total Billing SqFt
- Example: 85,000 Tk ÷ 15,000 sqft = 5.67 Tk/sqft

### ✅ Month Input
- User inputs month as integer (1-12)
- System stores as date (2025-11-01)
- Response includes `monthName` (e.g., "November")

### ✅ Enable/Disable Charges
- Each charge type can be enabled/disabled independently
- Disabled charges have zero rates

### ✅ Special Charges
- Optional special charges with custom name and remarks
- Can be enabled/disabled like other charges

### ✅ Lock Protection
- Locked configurations cannot be updated or deleted
- Must unlock first to make changes

### ✅ Manual/Auto SqFt
- `useAutoCalculation: true` - Calculate from all active shops
- `useAutoCalculation: false` - Use manual input

---

## 🧪 All Tests Passed ✅

1. ✅ POST - Create monthly cost
2. ✅ GET - List all/filter by year/month
3. ✅ GET - Get single by ID
4. ✅ PUT - Update configuration
5. ✅ DELETE - Remove configuration
6. ✅ POST - Lock configuration
7. ✅ POST - Unlock configuration
8. ✅ Rate auto-calculation working
9. ✅ Month name generation working
10. ✅ Lock protection working

---

## 📦 Files Created

1. `MonthlyCost.java` - Entity with all fields and auto-calculation logic
2. `MonthlyCostRepository.java` - JPA repository with custom queries
3. `MonthlyCostCreateDTO.java` - Request DTO for creating
4. `MonthlyCostUpdateDTO.java` - Request DTO for updating
5. `MonthlyCostService.java` - Business logic layer
6. `MonthlyCostController.java` - REST API endpoints

---

## 🚀 Ready to Use!

The Monthly Cost Management API is fully functional and tested. You can now:

1. Create monthly cost configurations with year/month
2. View configurations with filters
3. Update configurations (if not locked)
4. Delete configurations (if not locked)
5. Lock/unlock configurations
6. All rates are auto-calculated from total costs

**Your frontend team can start integrating these APIs immediately!**