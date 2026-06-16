# Engineer's Decor & Controls Reforged Reconstruction

This is a separate reconstruction workspace created from the published CurseForge `engineers_decor_reforged-1.1.jar` artifact.

Original recovered artifacts remain untouched in:

`C:\Users\Emmanuel Tremblay\AI Depot\Codex Documents\Engineer Decor Controls Reforged Recovery\artifacts`

Status:

- Decompiled Java source was generated with Vineflower.
- Resources were copied from the recovered JAR extraction.
- The original Gradle source project was not found locally.
- Decompiled Java must be reviewed and validated before treating this as the authoritative source project.
- Gradle `build` succeeds after correcting two decompiler artifacts in the reconstruction copy:
  - removed unreachable `break` statements after `yield` in `MachineBlockEntity.java`
  - removed raw `BlockEntityType` casts in capability registration in `EngineersDecorReforged.java`

Validation targets:

```powershell
.\gradlew.bat compileJava
.\gradlew.bat runGameTestServer
.\gradlew.bat build
```

Automation I/O fix:

- Small electrical furnace input slots are insertion-only for automation.
- Automation extraction is limited to layout result slots when a machine layout defines result slots.
- Regression coverage was added to `MachineGuiGameTests`.
- `runGameTestServer` now completes successfully; the latest local run passed 115 required GameTests.

Additional bug hunt fixes:

- Enabled NeoForge's milk fluid during mod construction so the Small Milking Machine can expose stored milk to fluid pipes.
- Mapped internal `"milk"` storage to `NeoForgeMod.MILK` for fluid handler fill/drain/read operations.
- Restored downward milk fluid push from the Small Milking Machine into tanks or pipes below.
- Added GameTest coverage for milk fluid exposure and downward tank transfer.
- Restricted specialized fluid machines to their intended fluids:
  - Small Milking Machine: milk only.
  - Small Mineral Smelter: lava only.
  - Passive Fluid Accumulator: water only.
- Restored processing-phase comparator output for the Small Mineral Smelter and Small Freezer.
