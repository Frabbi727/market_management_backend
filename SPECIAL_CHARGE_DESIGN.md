# Special Charge Feature Design

## Concept
**Special Charge** = Admin can add custom/one-time charges that don't fit into standard categories (Electricity, AC, Service, Generator, WiFi).

---

## Two Approaches:

### Approach 1: **Special Charge in ChargeConfig** (Applies to ALL shops)
Global special charge for the period - everyone pays it.

### Approach 2: **Special Charge per Invoice** (Shop-specific)
Individual charges for specific shops only.

---

## 🎯 Recommended: **BOTH Approaches** (Maximum Flexibility)

### Use Case Scenarios:

#### Scenario A: Global Special Charge (Apply to ALL shops)
```
Example: Security upgrade fee for the market
- Admin sets in ChargeConfig:
  - Special Charge Name: "Security System Upgrade"
  - Type: FIXED (100 Tk for all)
  - OR Type: RATE (0.50 Tk/sqft)

Result: Every shop's July bill includes this charge
```

#### Scenario B: Shop-Specific Special Charge
```
Example: Broken door repair for Shop 118 only
- Admin adds to Invoice #118:
  - Description: "Door repair charges"
  - Amount: 500 Tk

Result: Only Shop 118 pays this in July bill
```

---

## Database Design

### 1. ChargeConfig Table (Global Special Charge)

```sql
ALTER TABLE charge_config ADD COLUMN special_charge_enabled BOOLEAN DEFAULT FALSE;
ALTER TABLE charge_config ADD COLUMN special_charge_name VARCHAR(200);
ALTER TABLE charge_config ADD COLUMN special_charge_type VARCHAR(20); -- 'FIXED' or 'RATE'
ALTER TABLE charge_config ADD COLUMN special_charge_rate DECIMAL(10,2);
ALTER TABLE charge_config ADD COLUMN special_charge_fixed_amount DECIMAL(10,2);
ALTER TABLE charge_config ADD COLUMN special_charge_description VARCHAR(500);
```

**Example Data:**
```json
{
  "period": "2025-07-01",
  "specialChargeEnabled": true,
  "specialChargeName": "Security System Upgrade",
  "specialChargeType": "FIXED",
  "specialChargeFixedAmount": 100.00,
  "specialChargeDescription": "One-time security camera installation fee"
}
```

### 2. InvoiceItem Table (Shop-Specific Special Charges)

Already designed! Just use `chargeType = "SPECIAL"` and `isManual = true`

```sql
CREATE TABLE invoice_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    invoice_id BIGINT NOT NULL,
    charge_type VARCHAR(50),           -- 'SPECIAL'
    description VARCHAR(255),          -- 'Door repair charges'
    rate DECIMAL(10,2),
    unit_type VARCHAR(20),             -- 'fixed'
    quantity DECIMAL(10,2),            -- 1
    amount DECIMAL(10,2),              -- 500.00
    is_manual BOOLEAN DEFAULT TRUE,    -- TRUE for admin-added
    created_at TIMESTAMP,
    FOREIGN KEY (invoice_id) REFERENCES invoices(id)
);
```

---

## How It Works

### Admin UI - ChargeConfig Form (Global Special Charge)

```
┌─────────────────────────────────────────────────────────┐
│ Charge Configuration - July 2025                        │
├─────────────────────────────────────────────────────────┤
│                                                          │
│ Electricity Rate: 15.20 Tk/kwh        [✓] Enabled       │
│ AC Charge: ○ Rate  ● Fixed            [✓] Enabled       │
│   Rate: 5.48 Tk/sqft  Fixed: 740 Tk                     │
│ Service Charge: ● Rate  ○ Fixed       [✓] Enabled       │
│   Rate: 12.00 Tk/sqft  Fixed: 1620 Tk                   │
│ Generator: ○ Rate  ● Fixed            [✓] Enabled       │
│   Rate: 0.31 Tk/sqft  Fixed: 42 Tk                      │
│ WiFi: ○ Rate  ● Fixed                 [□] Enabled       │
│   Rate: 0 Tk/sqft     Fixed: 50 Tk                      │
│                                                          │
│ ┌────────────────────────────────────────────────────┐  │
│ │ Special Charge (Optional)          [✓] Enable     │  │
│ │                                                    │  │
│ │ Charge Name:                                      │  │
│ │ [Security System Upgrade_________________]        │  │
│ │                                                    │  │
│ │ Type:  ● Fixed Amount   ○ Rate per SqFt          │  │
│ │                                                    │  │
│ │ Fixed Amount: [100.00__] Tk                       │  │
│ │ Rate per SqFt: [0.00____] Tk/sqft                 │  │
│ │                                                    │  │
│ │ Description:                                      │  │
│ │ [One-time security camera installation fee       │  │
│ │  ____________________________________________]    │  │
│ └────────────────────────────────────────────────────┘  │
│                                                          │
│ [Save Configuration]  [Copy from Previous Month]        │
└─────────────────────────────────────────────────────────┘
```

### Admin UI - Individual Invoice Edit (Shop-Specific Special Charge)

```
┌─────────────────────────────────────────────────────────┐
│ Edit Invoice - Shop 118 - July 2025                     │
├─────────────────────────────────────────────────────────┤
│                                                          │
│ Owner: MR. ABU TAHER                                    │
│ Shop Area: 135 sqft                                     │
│                                                          │
│ ┌────────────────────────────────────────────────────┐  │
│ │ Auto-Calculated Charges                           │  │
│ │ ✓ Electricity: 76.00 Tk                            │  │
│ │ ✓ Generator: 42.00 Tk                              │  │
│ │ ✓ AC: 740.00 Tk                                    │  │
│ │ ✓ Service: 1,620.00 Tk                             │  │
│ │ ✓ Security System Upgrade: 100.00 Tk (Global)     │  │
│ └────────────────────────────────────────────────────┘  │
│                                                          │
│ ┌────────────────────────────────────────────────────┐  │
│ │ Manual/Special Charges                            │  │
│ │                                                    │  │
│ │ [+ Add Special Charge]                            │  │
│ │                                                    │  │
│ │ 1. Door Repair Charges        500.00 Tk  [Remove] │  │
│ │ 2. Extra Cleaning Fee         200.00 Tk  [Remove] │  │
│ └────────────────────────────────────────────────────┘  │
│                                                          │
│ Subtotal: 3,278.00 Tk                                   │
│ Adjustments: 0.00 Tk                                    │
│ Total: 3,278.00 Tk                                      │
│                                                          │
│ [Save Changes]  [Lock Bill]  [Generate PDF]             │
└─────────────────────────────────────────────────────────┘
```

### Add Special Charge Modal

```
┌─────────────────────────────────────────┐
│ Add Special Charge                      │
├─────────────────────────────────────────┤
│                                         │
│ Description:                            │
│ [Door repair charges____________]      │
│                                         │
│ Amount (Tk):                            │
│ [500.00_________]                       │
│                                         │
│ Remarks (Optional):                     │
│ [Broken during unloading________       │
│  ______________________________]       │
│                                         │
│         [Cancel]  [Add Charge]          │
└─────────────────────────────────────────┘
```

---

## Generated Bill Example

```
┌─────────────────────────────────────────────────────────┐
│         Paramount Market Shop Owners Association        │
│              65/2/1 Box Culvert Rd, Purana Paltan      │
│                  Bill Month: July - 2025                │
│     Issue Dt: 05-08-2025, Last dt of Payment: 25-08-2025│
├─────────────────────────────────────────────────────────┤
│                                                          │
│ Owner's Name: MR. ABU TAHER                             │
│ Shop No: 118                                            │
│ Meter No: 605762                                        │
│ Present Reading: 2,417 kwh                              │
│ Previous Reading: 2,412 kwh                             │
│                                                          │
├─────────────────────────────────────────────────────────┤
│ Current Charges                          Amount (Tk.)   │
├─────────────────────────────────────────────────────────┤
│ Electric Charges @Tk 15.20 /kwh × 5             76.00   │
│ Generator Fuel (Fixed)                          42.00   │
│ Central AC Electric 5.48 per sft × 135         740.00   │
│ Service Charges 12.00 per sft × 135          1,620.00   │
│ Security System Upgrade (Fixed)                100.00   │ ← Global
│ Door Repair Charges                            500.00   │ ← Shop-specific
│                                                          │
├─────────────────────────────────────────────────────────┤
│ Current Month's Total Bill Amount            3,078.00   │
├─────────────────────────────────────────────────────────┤
│ Previous Arrears/Adjustments                       0.00 │
├─────────────────────────────────────────────────────────┤
│ Total Amount to be Paid                      3,078.00   │
└─────────────────────────────────────────────────────────┘
```

---

## API Endpoints

### 1. ChargeConfig with Special Charge (Global)

**POST/PUT** `/api/v1/charge-config`

```json
{
  "marketId": 1,
  "period": "2025-07-01",
  "electricityRate": 15.20,
  "acChargeType": "RATE",
  "acRate": 5.48,
  "serviceChargeType": "RATE",
  "serviceRate": 12.00,
  "generatorChargeType": "FIXED",
  "generatorFixedAmount": 42.00,

  "specialChargeEnabled": true,
  "specialChargeName": "Security System Upgrade",
  "specialChargeType": "FIXED",
  "specialChargeFixedAmount": 100.00,
  "specialChargeDescription": "One-time security camera installation"
}
```

### 2. Add Shop-Specific Special Charge

**POST** `/api/v1/invoices/{invoiceId}/items`

```json
{
  "chargeType": "SPECIAL",
  "description": "Door repair charges",
  "amount": 500.00,
  "remarks": "Broken during unloading",
  "isManual": true
}
```

---

## Complete ChargeConfig Structure

```java
@Entity
public class ChargeConfig {
    // ... existing fields ...

    // ========== SPECIAL CHARGE (Optional, Global for all shops) ==========
    @Column(name = "special_charge_enabled")
    private Boolean specialChargeEnabled = false;

    @Column(name = "special_charge_name", length = 200)
    private String specialChargeName; // "Security System Upgrade"

    @Column(name = "special_charge_type", length = 20)
    private String specialChargeType = "FIXED"; // 'FIXED' or 'RATE'

    @Column(name = "special_charge_rate", precision = 10, scale = 2)
    private BigDecimal specialChargeRate; // Tk/sqft (if RATE)

    @Column(name = "special_charge_fixed_amount", precision = 10, scale = 2)
    private BigDecimal specialChargeFixedAmount; // Fixed amount (if FIXED)

    @Column(name = "special_charge_description", length = 500)
    private String specialChargeDescription;

    // Helper method
    public BigDecimal calculateSpecialCharge(BigDecimal shopSqft) {
        if (!specialChargeEnabled) return BigDecimal.ZERO;
        if ("FIXED".equals(specialChargeType)) {
            return specialChargeFixedAmount != null ? specialChargeFixedAmount : BigDecimal.ZERO;
        }
        return specialChargeRate != null ? specialChargeRate.multiply(shopSqft) : BigDecimal.ZERO;
    }
}
```

---

## Bill Generation Logic

```java
public Invoice generateInvoice(Shop shop, ChargeConfig config, LocalDate period) {
    Invoice invoice = new Invoice();
    BigDecimal shopArea = shop.getAreaSqft();

    // 1. Standard charges (electricity, AC, service, generator, wifi)
    // ... existing logic ...

    // 2. GLOBAL Special Charge (if enabled in config)
    if (config.getSpecialChargeEnabled()) {
        BigDecimal amount = config.calculateSpecialCharge(shopArea);
        invoice.setSpecialAmount(amount); // Add field to Invoice

        InvoiceItem item = new InvoiceItem();
        item.setChargeType("SPECIAL");
        item.setDescription(config.getSpecialChargeName());
        item.setAmount(amount);
        item.setIsManual(false); // Auto-generated from config
        invoice.addItem(item);
    }

    // 3. Shop-specific special charges are added manually by admin later

    return invoice;
}
```

---

## Invoice Entity Update

```java
@Entity
public class Invoice {
    // ... existing fields ...

    @Column(name = "special_amount", precision = 10, scale = 2)
    private BigDecimal specialAmount = BigDecimal.ZERO;

    // Recalculate total
    public void calculateTotal() {
        this.subtotal = electricityAmount
            .add(acAmount)
            .add(serviceAmount)
            .add(generatorAmount)
            .add(wifiAmount)
            .add(specialAmount); // Include special charge

        this.total = subtotal.add(adjustmentAmount);
        this.dueAmount = total.subtract(paidAmount);
    }
}
```

---

## Summary

### ✅ Two Types of Special Charges:

1. **Global Special Charge** (ChargeConfig)
   - Applies to ALL shops
   - Set once in config
   - Can be FIXED or RATE-based
   - Auto-added during bill generation

2. **Shop-Specific Special Charge** (InvoiceItem)
   - Applies to individual shop
   - Manually added by admin
   - Always fixed amount
   - Added after bill generation

### ✅ Flexibility:
- Admin can enable/disable global special charge
- Admin can choose FIXED or RATE calculation
- Admin can add unlimited shop-specific charges
- Each charge has description/remarks

### ✅ Bill Display:
Both types show in the bill, clearly labeled so shop owner understands the charge.

---

## Should I Implement This?

This gives you **maximum flexibility**:
- Market-wide special fees (security, maintenance)
- Individual shop penalties/repairs
- Seasonal charges
- One-time assessments

Ready to build? Let me know! 🚀