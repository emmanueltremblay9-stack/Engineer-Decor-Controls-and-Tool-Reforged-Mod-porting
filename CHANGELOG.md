# Changelog

All notable reconstruction, repair, and validation changes for this project are recorded here.

## [1.1.9-reconstructed] - 2026-06-17

### Validation

- Confirmed `runGameTestServer` passes with all 146 required GameTests.
- Confirmed `clean build` passes and produces `build/libs/engineers_decor_reforged-1.1.9-reconstructed.jar`.
- Installed the rebuilt jar into the Prism `1.21.1 TesT LaB` instance with matching SHA-256 hashes.

### Changed

- Added Immersive Engineering Industrial Hemp Seeds (`immersiveengineering:seed`) to `engineers_decor_reforged:musli_bar_press_seeds`.
- Muslee Bar Press inventory use and both Muslee recipes now accept Industrial Hemp Seeds through the existing shared accepted-seed tag.
- Updated Muslee Bar Press tooltip and manual text to mention hemp seeds.
- Synchronized user-visible mod version metadata to `1.1.9-reconstructed`.

### Added

- Extended Muslee Bar Press seed GameTest coverage to include the Immersive Engineering hemp seed item.

## [1.1.8-reconstructed] - 2026-06-17

### Validation

- Confirmed `runGameTestServer` passes with all 146 required GameTests.
- Confirmed `clean build` passes and produces `build/libs/engineers_decor_reforged-1.1.8-reconstructed.jar`.
- Installed the rebuilt jar into the Prism `1.21.1 TesT LaB` instance with matching SHA-256 hashes.

### Fixed

- Restored Muslee Bar Press seed parity from Engineer's Tools metadata and readme:
  - Inventory use now accepts wheat, melon, pumpkin, and beetroot seeds instead of wheat seeds only.
  - The Muslee Bar shapeless recipe and Muslee Bar Press shaped recipe now share the same accepted-seed item tag.
  - Output-space simulation now treats any accepted seed as the consumed seed slot, preserving the atomic no-loss behavior for full inventories.
- Updated Muslee Bar Press tooltip and manual text so the documented seed options match runtime behavior.
- Synchronized user-visible mod version metadata to `1.1.8-reconstructed`.

### Added

- Added the `engineers_decor_reforged:musli_bar_press_seeds` item tag.
- Added GameTest coverage for melon, pumpkin, and beetroot seed press use.

## [1.1.7-reconstructed] - 2026-06-17

### Validation

- Confirmed `runGameTestServer` passes with all 145 required GameTests.
- Confirmed `clean build` passes and produces `build/libs/engineers_decor_reforged-1.1.7-reconstructed.jar`.
- Installed the rebuilt jar into the Prism `1.21.1 TesT LaB` instance with matching SHA-256 hashes.

### Fixed

- Restored original Ariadne Coal parity from Engineer's Tools:
  - Ariadne Coal is now a single-stack durability tool instead of a stack of consumable marker items.
  - Ariadne Coal now uses the original 100-use durability.
  - Successful marker placement now costs one durability and breaks the item on the final use.
  - Failed marker placement now leaves durability unchanged.
  - Placement feedback now uses the original hit/break style sounds instead of the placeholder stone-place sound.
- Updated Ariadne Coal tooltip and manual text so the documented cost matches the restored durability behavior.
- Synchronized user-visible mod version metadata to `1.1.7-reconstructed`.

### Added

- Updated Ariadne Coal GameTests for single-stack behavior, original durability, placement wear, failed-use no-wear behavior, and final-use break behavior.

## [1.1.6-reconstructed] - 2026-06-17

### Validation

- Confirmed `runGameTestServer` passes with all 144 required GameTests.
- Confirmed `clean build` passes and produces `build/libs/engineers_decor_reforged-1.1.6-reconstructed.jar`.
- Installed the rebuilt jar into the Prism `1.21.1 TesT LaB` instance with matching SHA-256 hashes.

### Fixed

- Restored original Mind Squeezer parity from Engineer's Tools:
  - the Mind Squeezer is stackable to 64 and no longer has durability or breaks
  - creative/instabuild no longer bypasses the original lapis and XP requirements
  - low-health refusal now uses the original `maxHealth / 10` threshold
  - successful conversion now consumes one lapis and one XP level, applies the original `maxHealth / 10` health cost, adds hunger exhaustion, and briefly blinds the user
  - success sounds now match the original hurt/enchantment feedback instead of the placeholder grindstone sound
- Updated Mind Squeezer tooltip and manual text so the documented costs match the restored behavior.
- Synchronized user-visible mod version metadata to `1.1.6-reconstructed`.

### Added

- Updated GameTest coverage for Mind Squeezer stackability, non-durability, original costs/effects, low-health threshold behavior, and creative no-bypass behavior.

## [1.1.5-reconstructed] - 2026-06-17

### Validation

- Confirmed `runGameTestServer` passes with all 144 required GameTests.
- Confirmed `clean build` passes and produces `build/libs/engineers_decor_reforged-1.1.5-reconstructed.jar`.
- Installed the rebuilt jar into the Prism `1.21.1 TesT LaB` instance with matching SHA-256 hashes.

### Fixed

- Restored additional original Engineer's Tools parity:
  - Sleeping Bag now uses the original 4096 durability instead of 48.
  - Sleeping Bag keeps its original hidden durability bar.
  - Crushing Hammer entity hits now cancel vanilla attack damage after applying knockback.
  - Crushing Hammer now disables shields like the original item.
  - Crushing Hammer block mining now returns the original no-use-stat result after server-side wear handling.
- Synchronized user-visible mod version metadata to `1.1.5-reconstructed`.

### Added

- Added GameTest coverage for Sleeping Bag durability/bar behavior and updated Crushing Hammer attack/shield parity coverage.

## [1.1.4-reconstructed] - 2026-06-17

### Validation

- Confirmed `runGameTestServer` passes with all 143 required GameTests.
- Confirmed `clean build` passes and produces `build/libs/engineers_decor_reforged-1.1.4-reconstructed.jar`.
- Installed the rebuilt jar into the Prism `1.21.1 TesT LaB` instance with matching SHA-256 hashes.

### Fixed

- Restored original Charged Lapis parity from Engineer's Tools:
  - heals `maxHealth / 20` instead of a fixed 3 HP
  - clears fire on use
  - keeps the original foil item glint
- Synchronized user-visible mod version metadata to `1.1.4-reconstructed`.

### Added

- Added GameTest coverage for Charged Lapis healing, fire clearing, XP grant, item consumption, and foil behavior.

## [1.1.1-reconstructed] - 2026-06-17

### Validation

- Confirmed `compileJava` passes.
- Confirmed `runGameTestServer` passes with all 139 required GameTests.
- Confirmed `clean build` passes and produces `build/libs/engineers_decor_reforged-1.1.1-reconstructed.jar`.
- Installed the rebuilt jar into the Prism `1.21.1 TesT LaB` instance with matching SHA-256 hashes.

### Fixed

- Reimplemented the REDIA Tool against the original MIT Engineer's Tools behavior:
  - diamond-grade axe/pickaxe/shovel/hoe/shears capability exposure
  - original 3000 durability
  - normal-use torch placement from inventory without REDIA durability loss
  - sneak-use plant snipping, ground cycling, and axe stripping
  - sheep/entity shearing
  - villager, owned-pet, and neutral zombified piglin safe-attack cancellation
  - sneak-break connected log felling
  - durability-dependent Efficiency/Fortune decay
- Added the REDIA diamond repair/over-repair recipe and serializer so over-repair restores the original Efficiency/Fortune progression.
- Synchronized user-visible mod version metadata to `1.1.1-reconstructed`.

### Added

- Added GameTest coverage for REDIA multi-tool actions, ground cycling, plant snipping, entity shearing, safe attack, tree felling, and diamond over-repair.
- Added `install-mod.ps1` to reproducibly build/install the runtime jar and write `build/install-report.json`.

## [1.1-reconstructed] - 2026-06-17

### Validation

- Confirmed `compileJava` passes.
- Confirmed `runGameTestServer` passes with all 132 required GameTests.
- Confirmed `build` passes and produces `build/libs/engineers_decor_reforged-1.1-reconstructed.jar`.
- Latest rebuilt jar observed locally: `2,450,208` bytes, updated `2026-06-17 3:21:21 PM`.

### Added

- Reconstructed a NeoForge 1.21.1 Gradle source project from the published `engineers_decor_reforged-1.1.jar`.
- Restored recovered resources, Gradle wrapper files, mod metadata, recipes, loot tables, models, blockstates, language files, and manual assets.
- Added the project logo as the root `pack.png` asset and NeoForge mod-list `logoFile`.
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
  - metal sliding door hitboxes matching the visible closed/open models
  - machine menu progress meters reporting normalized percentages
  - tracker tooltips ignoring incomplete saved target data
  - metal rung ladders and staggered metal steps registered as climbable blocks

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
- Replaced the metal sliding door's vanilla hinged-door shape with model-aligned sliding-door shapes for closed and open states.
- Normalized machine menu progress reporting for lab furnaces, electrical furnaces, temperature machines, and block breakers.
- Synced Small Block Breaker process totals through menu data so progress percentages use the real configured work time.
- Suppressed Tracker location tooltips when saved target custom data has a dimension but incomplete coordinates.
- Added Metal Rung Ladder and Staggered Metal Steps to the vanilla `minecraft:climbable` block tag so player ladder movement works in-game.

### Repository

- Published initial reconstructed source import in commit `58fb6e4`.
- Published machine/control/tool regression repairs in commit `abda90c`.
- Published REDIA Tool and pulse-control fixture repairs in commit `78489ce`.
- Published factory hopper pulse collection and GameTest footprint repairs in commit `838a0cb`.
- Published mod logo asset in commit `489efa6`.
- Published menu progress and tracker tooltip repairs in commit `330dce5`.

### Known Follow-Up

- Review whether Gradle project version `1.1-reconstructed` and `neoforge.mods.toml` display version `1.1` should be aligned for release packaging.
- Continue bug hunting in machine automation, fluid handlers, redstone support/drop rules, save/load normalization, menu shift-click behavior, and tool edge cases.
