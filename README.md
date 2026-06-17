# Engineer's Decor & Controls Reforged

Reconstructed NeoForge 1.21.1 source project for Engineer's Decor & Controls Reforged.

This workspace was rebuilt from the published `engineers_decor_reforged-1.1.jar` artifact, with decompiled Java reviewed and repaired into a buildable Gradle project. It includes the recovered resources, Gradle wrapper, source, and GameTest regression coverage for the reconstructed behavior.

## Validation

```powershell
.\gradlew.bat compileJava
.\gradlew.bat runGameTestServer
.\gradlew.bat build
```

Latest local validation before publishing:

- `compileJava` passed.
- `runGameTestServer` passed with 129 required GameTests.
- `build` passed and produced `build/libs/engineers_decor_reforged-1.1-reconstructed.jar`.
- Latest rebuilt jar observed locally: `2,322,252` bytes, updated `2026-06-16 11:18:26 PM`.

## Notes

See `README_RECONSTRUCTION.md` for reconstruction provenance and `CHANGELOG.md` for release-style repair notes.
