
application를 분리할 경우, flux 모델로 변경.

// admin login
Redis로 RefreshToken 저장	만료 자동화, 속도 향상
블랙리스트 처리	AccessToken 강제 무효화 (JWT stateless 구조 대응용)
토큰 재사용 감지	동일 RefreshToken 재사용 → 로그 추적 가능

// local = compose, dev, prd = Amazon RDS로 인프라 구현할 것


// 고아 파일이 생기지 않도록 롤링 설계
1. Presigned URL 발급과 DB 예약 저장 분리	업로드 전에 DB에 "예약 레코드" 저장
2. 주기적 정합성 검사(batch)	S3와 DB를 비교하여 고아 파일 정리
3. 업로드 실패 감지 및 TTL 설정	presigned URL로 업로드 안 된 파일은 일정 시간 후 제거
4. S3 event notification + Lambda 연동	파일 업로드 성공 시 Lambda로 DB 반영 (비동기 방식)
5. S3 lifecycle 정책 활용	고아 파일을 자동으로 삭제 (예: 1일 안에 DB에 등록되지 않으면 삭제)