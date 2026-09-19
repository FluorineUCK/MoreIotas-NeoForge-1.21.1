# NeoForge / Hex Casting pre53 port

Minecraft **1.21.1**, NeoForge **21.1.234**, Java **21**, Hex Casting **0.12.0-devel-pre-53** exactly.

Upstream: https://github.com/FallingColors/MoreIotas.git

Accepted upstream commit: `0d96e56c3cff5657cdeeb8394ba7b0c9c6a48387`. Previous published branch is retained as `pre39`; this branch starts at `dc7fd8cb32612856e318553b6cb143a2098ca46f`.

## Release artifacts

| Mod ID | File | SHA-256 |
|---|---|---|
| `moreiotas` | `moreiotas-0.1.1+1.21.1-neoforge-pre53.jar` | `04fd53fddfa8c21a448becd25ec0692c02dc076ddfc745eaa700705fd9474e23` |

## Building

This source tree mirrors the validated development project. Third-party JARs and local Gradle caches are intentionally not committed. Existing `libs/` references and sibling-workspace build inputs must be supplied as declared by the Gradle scripts; an empty standalone clone is not claimed to be a complete offline build environment. The Release contains the validated ordinary runtime JAR(s), not API/source/test JARs.

Upstream CI workflows targeting other loaders are omitted from this publication branch rather than allowed to publish unrelated artifacts.

## Verification

Per-mod core tests were completed in the development workspace. The frozen cohort additionally passed dedicated-server/real-client loading, registered dynamic-registry spell execution, TCP interaction, and native save/new-JVM restore. This is not an assertion that every possible gameplay permutation was tested.

## Documentation and attribution

Original root licenses and attributed `upstream-docs/` are retained. That directory preserves the historical documentation snapshot from the prior publication; the accepted current source commit is stated above. Inherited upstream build/loader instructions may describe a different platform.


## Runtime dependency declarations

These rows are extracted from the released JAR metadata; the tested cohort uses NeoForge 21.1.234 even when a declaration allows an earlier minimum.

| Mod | Dependency | Type | Version range | Side |
|---|---|---|---|---|
| `moreiotas` | `neoforge` | required | `[21.1.234,)` | BOTH |
| `moreiotas` | `minecraft` | required | `[1.21.1]` | BOTH |
| `moreiotas` | `hexcasting` | required | `[0.12.0-devel-pre-53]` | BOTH |
| `moreiotas` | `paucal` | required | `[0.7.1-pre-27,)` | BOTH |
| `moreiotas` | `patchouli` | required | `[1.21.1-92-NEOFORGE,)` | BOTH |
| `moreiotas` | `kotlinforforge` | required | `[5.12,)` | BOTH |
