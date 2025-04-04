
---

### 1. File Manager Service README.md

```markdown
# File Manager Service

AWS S3 дээрх файлуудыг удирдахад зориулсан RESTful сервис. Энэ сервис нь файлуудыг хадгалах, татах, устгах, жагсаах зэрэг үйлдлүүдийг гүйцэтгэдэг.

## Онцлог
- AWS S3 bucket-д файлуудыг хадгалах (`upload`).
- Тодорхой хавтас дахь файлуудыг жагсаах (`list`).
- Файлыг татах (`download`) болон устгах (`delete`).
- Файлын мета өгөгдлийг харах (`metadata`).

## Технологийн Стек
- **Java**: Spring Boot
- **AWS SDK**: S3 интеграци
- **Nginx**: Reverse Proxy (сонголттой)

## Суулгах Заавар

### Шаардлага
- Java 11+ (JDK)
- Maven
- AWS S3 bucket ба credentials (Access Key, Secret Key)
- Ubuntu сервер (сонголттой, deploy-д)

### Алхмууд
1. Репозиторийг клон хийнэ:
   ```bash
   git clone https://github.com/[your-username]/file-manager-service.git
   cd file-manager-service
   ```
2. AWS credentials-г тохируулна (`application.properties`):
   ```properties
   aws.s3.bucket=your-bucket-name
   aws.access-key-id=your-access-key
   aws.secret-access-key=your-secret-key
   ```
3. Build хийнэ:
   ```bash
   mvn clean package
   ```
4. Сервисийг ажиллуулна:
   ```bash
   java -jar target/file-manager-service-0.0.1-SNAPSHOT.jar
   ```

### Nginx Reverse Proxy (Сонголттой)
Nginx-ийг reverse proxy болгон ашиглах бол:
```nginx
server {
    listen 80;
    server_name <your-public-ip-or-domain>;

    location / {
        proxy_pass http://localhost:8082;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```
Тохиргоог идэвхжүүлэх:
```bash
sudo ln -s /etc/nginx/sites-available/file-manager /etc/nginx/sites-enabled/
sudo systemctl reload nginx
```

## API Endpoint-ууд
- **GET** `/api/files/list?folder=<folder-name>`: Хавтас дахь файлуудыг жагсаана.
- **POST** `/api/files/upload?folder=<folder-name>`: Файлыг S3-д хадгална.
- **GET** `/api/files/<folder>/<filename>`: Файлыг татна.
- **DELETE** `/api/files/<folder>/<filename>`: Файлыг устгана.

### Жишээ Хүсэлт
Хавтас дахь файлуудыг жагсаах:
```bash
curl http://<your-ip>:8082/api/files/list?folder=marchDogPhotos
```

## Хэрэглээ
Энэ сервисийг `Mailer Service`-тэй хамт ашиглан S3 дээрх зургуудыг имэйлээр илгээх боломжтой.

## Лиценз
MIT License
```

---

