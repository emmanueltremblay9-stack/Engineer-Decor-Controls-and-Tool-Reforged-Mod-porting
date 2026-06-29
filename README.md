# Immersive Engineer Decor&Controls&Tool Reforged

Reconstructed NeoForge 1.21.1 source project for Immersive Engineer Decor&Controls&Tool Reforged.

This workspace was rebuilt from the published `engineers_decor_reforged-1.1.jar` artifact, with decompiled Java reviewed and repaired into a buildable Gradle project. It includes the recovered resources, Gradle wrapper, source, and GameTest regression coverage for the reconstructed behavior.

## Validation

```powershell
.\gradlew.bat compileJava
.\gradlew.bat runGameTestServer
.\gradlew.bat clean build
```

Latest local validation before publishing:

- `compileJava` passed.
- `runGameTestServer` passed with 167/167 required GameTests.
- `clean build` passed.
- Latest rebuilt jar observed locally: `build/libs/immersive_engineer_decor_controls_tool_reforged-1.1.28-reconstructed.jar`, 2,653,649 bytes, SHA-256 `68A1168AF9A115ED84291586FD2675E1FF33A85A7280654DCB196DFC299D8FDA`.
- `scripts/audit-resource-parity.ps1` passed: every deleted `engineers_decor_reforged` resource has an equivalent `immersive_engineer_decor_controls_tool_reforged` replacement, and current resource roots contain no legacy namespace references.
- Prism LAB installation has not been run for this release prep.

## Config status

The existing COMMON config flags are registered, but they are not currently used to disable registered blocks, items, menus, or other content. Do not treat them as functional content toggles until that behavior is implemented and validated.

## Notes

See `README_RECONSTRUCTION.md` for reconstruction provenance and `CHANGELOG.md` for release-style repair notes.
