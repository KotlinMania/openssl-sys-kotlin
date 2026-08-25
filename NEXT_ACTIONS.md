# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 5/68 (7.4%)
- **Function parity:** 0/57 matched (target 11) — 0.0%
- **Class/type parity:** 2/110 matched (target 2) — 1.8%
- **Combined symbol parity:** 2/167 matched (target 13) — 1.2%
- **Average inline-code cosine:** 0.80 (function body across 5 matched files)
- **Average documentation cosine:** 0.00 (doc text across 5 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 1 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. sha

- **Target:** `opensslsys.Sha [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 210.0
- **Functions:** 0/0 matched (target 11)
- **Missing functions:** _none_
- **Types:** 2/2 matched
- **Missing types:** _none_

### 2. aes

- **Target:** `opensslsys.Aes`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 3. asn1

- **Target:** `opensslsys.Asn1`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 4. srtp

- **Target:** `opensslsys.Srtp`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 5. core_dispatch

- **Target:** `opensslsys.CoreDispatch`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `handwritten.mod` | `handwritten.Mod` | 0 | `handwritten/mod.rs` | `handwritten/Mod.kt` |
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |

