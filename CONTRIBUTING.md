# Contributing to molo

Thank you for your interest in improving molo! Contributions of all kinds are
welcome: bug reports, documentation, translations and code.

## Reporting bugs and proposing features

Please open an issue and describe

- what you expected and what happened instead,
- steps to reproduce, including device, Android version and app version
  (never include secrets or personal data from the app),
- version or commit.

Please report security issues privately to [kontakt@molo.news](mailto:kontakt@molo.news)
or via GitHub's private vulnerability reporting instead of opening an issue.

## Development setup

Follow the [Getting started](README.md#getting-started) and
[Configuration](README.md#configuration) sections of the README. Open the project
in Android Studio or build from the command line with `./gradlew :app:assembleDebug`.

## Pull requests

1. Fork the repository and create a branch from `develop`.
2. Keep changes focused; one topic per pull request.
3. Make sure the project builds before you push:
   ```bash
   ./gradlew :app:assembleDebug
   ./gradlew lint
   ```
4. Describe what changed and why in the pull request.

## Guidelines

- **No secrets, personal data or instance-specific values in the code.** API keys,
  signing keys, tokens and `google-services.json` belong in `local.properties`,
  environment variables or files ignored by Git (see `.gitignore`). Never commit a
  keystore.
- **Architecture.** Keep the layer split: `domain` has no Android dependencies,
  `data` implements the repositories, each `feature` is split into `presentation`
  (state and view models) and `ui` (Compose). New features get their own modules.
- **Dependencies** are declared centrally in `gradle/libs.versions.toml`.
- Code comments and user-facing texts may be German or English; keep them
  consistent within a file. User-facing strings live in `core/resource`.

## License

By contributing, you agree that your contributions are licensed under the
[AGPL-3.0](LICENSE).
