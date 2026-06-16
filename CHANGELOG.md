# Changelog

All notable reconstruction, repair, and validation changes for this project are recorded here.

## [1.1-reconstructed] - 2026-06-16

### Validation

- Confirmed `compileJava` passes.
- Confirmed `runGameTestServer` passes with all 128 required GameTests.
- Confirmed `build` passes and produces `build/libs/engineers_decor_reforged-1.1-reconstructed.jar`.
- Latest rebuilt jar observed locally: `2,317,606` bytes, updated `2026-06-16 4:41:13 PM`.

### Added

- Reconstructed a NeoForge 1.21.1 Gradle source project from the published `engineers_decor_reforged-1.1.jar`.
- Restored recovered resources, Gradle wrapper files, mod metadata, recipes, loot tables, models, blockstates, language files, and manual assets.
- Added GameTest coverage for machine menus, automation transfer, machine save/load normalization, fluid handlers, redstone controls, accessway blocks, and selected tool behavior.
- Added regression coverage for:
  - attached redstone controls dropping when backing support is removed
  - negative and oversized machine runtime timers being clamped on load
  - powered iron hatches staying open while redstone-powered
  - block breaker and tree cutter drop buffering
  - factory hopper pulse redstone behavior
  - milking machine bucket and fluid output paths
  - direct filled-bucket fluid state updates
  - factory dropper duplicate filter handling
  - factory hopper comparator refresh after collection
  - comparator switch self-output isolation
  - material box recovery from invalid stored item data
  - REDIA Tool torch placement into replaceable blocks
  - GameTest fixture spacing for larger multi-block tests

### Fixed

- Removed decompiler artifacts that prevented clean compilation:
  - unreachable `break` statements after `yield` in `MachineBlockEntity.java`
  - raw `BlockEntityType` casts in capability registration
- Fixed automation extraction so machines with defined result slots expose output-only extraction and insertion-only input behavior where appropriate.
- Enabled NeoForge milk fluid and mapped internal `"milk"` tank storage to `NeoForgeMod.MILK`.
- Restored downward milk transfer from the Small Milking Machine into tanks or fluid handlers below.
- Restricted specialized fluid machines to their intended fluids:
  - Small Milking Machine accepts milk only.
  - Small Mineral Smelter accepts lava only.
  - Passive Fluid Accumulator accepts water only.
- Restored processing-phase comparator output for the Small Mineral Smelter and Small Freezer.
- Made attached redstone controls require sturdy backing support and drop when support is removed.
- Clamped malformed loaded machine runtime timers so invalid values cannot persist into runtime behavior.
- Kept powered iron hatches open when right-clicked while still receiving redstone power.
- Routed Small Block Breaker and Small Tree Cutter harvested drops into their internal drop buffers instead of spawning them into the world.
- Made factory hopper pulse mode respect redstone trigger semantics before collecting loose world items.
- Prevented factory hopper pulse mode from collecting while redstone power is merely held without a new edge.
- Allowed Small Milking Machine bucket output even when the internal tank is full, and cleared stale active state when no output/storage path exists.
- Updated visible fluid block state immediately after direct filled-bucket use.
- Cleared Small Tree Cutter active visual state immediately when redstone disables it during cooldown.
- Made factory dropper duplicate filters reserve simulated stock per match so a duplicate filter cannot emit a partial duplicate stack.
- Refreshed adjacent comparators after factory hopper world-item collection changes inventory.
- Prevented industrial comparator switches from latching from their own output.
- Normalized invalid Material Box `stored_item` custom data as empty so boxes can recover and accept valid materials again.
- Allowed REDIA Tool torch placement into replaceable blocks such as short grass while validating the resulting torch state before consuming resources.
- Added valid backing support to pulse-control GameTest fixtures.
- Enlarged the shared blank GameTest structure footprint to 8x8x16 so multi-block fixtures are spaced correctly.

### Repository

- Published initial reconstructed source import in commit `58fb6e4`.
- Published machine/control/tool regression repairs in commit `abda90c`.
- Published REDIA Tool and pulse-control fixture repairs in commit `78489ce`.
- Published factory hopper pulse collection and GameTest footprint repairs in commit `838a0cb`.

### Known Follow-Up

- Review whether Gradle project version `1.1-reconstructed` and `neoforge.mods.toml` display version `1.1` should be aligned for release packaging.
- Continue bug hunting in machine automation, fluid handlers, redstone support/drop rules, save/load normalization, menu shift-click behavior, and tool edge cases.
