# Diploma

## Local images for seed migrations

Seed cars use `image_url` keys like `seed:toyota-camry`.
After Liquibase migrations finish, the app can load local files from your computer and write compressed blobs to `dip_car.image_blob`.

### 1) Put files in a local folder

Use file names that match keys after `seed:`:

- `toyota-camry.jpg`
- `bmw-5-series.png`
- `mercedes-e-class.webp`

Supported formats: `.jpg`, `.jpeg`, `.png`, `.webp`, `.svg`.

### 2) Configure folder path

Set environment variable before app start:

```powershell
$env:DIPLOMA_SEED_IMAGES_DIR = "D:\car-seed-images"
```

Or set `app.seed.images.dir` directly in `application.properties`.

### 3) Start application

Liquibase runs first, then local loader updates blobs for rows where `image_url` starts with `seed:`.

## Compression behavior

- SVG: minified (whitespace/comments removed).
- Raster images: resized to fit `app.seed.images.max-width` / `app.seed.images.max-height`.
- JPEG output quality is controlled by `app.seed.images.jpeg-quality`.

Default settings are in `src/main/resources/application.properties`.

