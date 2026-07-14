# Run Commands

File này được duy trì tự động bởi skill `record-run-commands`, liệt kê lệnh Maven để chạy từng feature test trong `ui-tests`.

## Home Page (HomePageTest)

```bash
mvn -pl ui-tests test "-Dtest=HomePageTest" "-Denv=prod" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

- `env=prod`: chạy trên môi trường production của Vinamilk.
- `browser.headless=false`: mở trình duyệt có giao diện (không chạy ẩn).

## Authentication (LoginTest)

```bash
mvn -pl ui-tests test "-Dtest=LoginTest" "-Denv=prod" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

## Product Search (ProductSearchTest)

```bash
mvn -pl ui-tests test "-Dtest=ProductSearchTest" "-Denv=prod" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

## Shopping Cart (AddToCartTest)

```bash
mvn -pl ui-tests test "-Dtest=AddToCartTest" "-Denv=prod" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

## Chạy toàn bộ test suite

```bash
mvn -pl ui-tests test "-Denv=prod" "-Dselenium.grid.enabled=false" "-Dbrowser.headless=false"
```

## Common flags

| Flag | Ý nghĩa |
|---|---|
| `-Dtest=<ClassName>` | Chỉ chạy class test này (bỏ qua để chạy tất cả). |
| `-Denv=<prod\|staging\|dev>` | Môi trường target, quyết định base URL. |
| `-Dselenium.grid.enabled=<true\|false>` | `true`: chạy qua Selenium Grid; `false`: chạy driver local. |
| `-Dbrowser.headless=<true\|false>` | `true`: chạy ẩn (không mở cửa sổ browser); `false`: mở browser để xem trực tiếp. |
