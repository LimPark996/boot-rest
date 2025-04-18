### Diagram

```mermaid
sequenceDiagram
    note over 👤사용자: form에 name, desc 입력하고 제출

    👤사용자->>🌐JS: 사용자가 form 작성 후 submit
    note over 🌐JS: fetch('/api/animals', { method: 'POST', body: JSON })로 서버에 전송
    🌐JS->>🎯Controller: POST /api/animals\n{ name, description }

    note over 🎯Controller: @RestController인 AnimalController가 요청 받음\n→ @RequestBody로 JSON → AnimalRequestDTO 변환
    🎯Controller->>🎯Controller: new AnimalRequestDTO(name, description)

    note over 🎯Controller: DTO의 toAnimal() 메서드로 Entity 생성
    🎯Controller->>⚙️Service: animalService.save() 호출

    note over ⚙️Service: AnimalServiceImpl에서 animalMapper.insert(animal) 실행
    ⚙️Service->>📦Mapper: animalMapper.insert(entity)

    note over 📦Mapper: MyBatis의 @Insert 문으로 실제 SQL 실행
    📦Mapper->>🗄️DB: INSERT INTO animal (...)

    note over 🗄️DB: animal 테이블에 데이터 저장 완료!

    📦Mapper-->>⚙️Service: void (현재 insert는 return 없음)
    ⚙️Service-->>🎯Controller: 저장한 entity 반환 (현재 null)
    🎯Controller-->>🌐JS: JSON 응답
    🌐JS-->>👤사용자: 응답 확인 & 콘솔 출력 or UI 갱신
```

### 각 파일 역할 정리

계층 | 파일 | 역할 요약
🌍 HTML/JS | index.html | 사용자 입력 → fetch('/api/animals', { method: 'POST' })로 JSON 전송
🎯 Controller | AnimalController | REST API 진입 지점. @PostMapping으로 요청 받음 → DTO → Entity 변환 후 Service에 전달
🧠 DTO | AnimalRequestDTO | 클라이언트가 보낸 JSON 데이터를 객체로 받기 위한 형식. toAnimal()로 Entity 변환 지원
⚙️ Service | AnimalService, AnimalServiceImpl | 비즈니스 로직 담당. 현재는 insert만 있고, AI story는 아직 빈값
🧩 Mapper (MyBatis) | AnimalMapper | DB 접근 담당. SQL 직접 작성. @Insert로 animal 테이블에 저장
🗃️ 도메인 객체 | Animal | DB 테이블과 거의 1:1로 매핑되는 불변 객체 (record 사용)
