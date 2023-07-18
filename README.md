# android-slide
Android 학습 프로젝트 #2

> ## 1. 슬라이드 앱 프로젝트
> 230717 PM 3:30
> - 태블릿 레이아웃 대응 xml
> - 랜덤 ID를 뽑고 객체를 생성하는 팩토리 패턴, 일단 나머지 속성은 랜덤으로 생성.

> ## 2. 속성 변경 동작
> 230718 AM 11:10
> - Slide들을 가지고 있고, Slide들의 속성을 제어하는 기능은 모두 SlideManager에서 관리
> - MainActivity에서는 각 버튼의 처리를 SlideManager에게 넘기게 구현
> - 배경색 버튼 누르면, 현재 슬라이드의 색상이 랜덤으로 바뀌고 Hex값이 버튼에 출력
> - 투명도는 1~10 단계로 조절 가능하며 변경시 현재 슬라이드에 바로 적용
> - 투명도는 Mode enum클래스를 만들어서 PLUS/MINUS 동작 관리

| ![Screenshot_20230718_111702](https://github.com/softeerbootcamp-2nd/android-slide/assets/61905052/f72eb6c0-eacb-4776-82aa-fb7ceb458654) | ![Screenshot_20230718_111714](https://github.com/softeerbootcamp-2nd/android-slide/assets/61905052/033e27d1-67e0-4004-b073-bfabb7e1b20b) | ![Screenshot_20230718_111721](https://github.com/softeerbootcamp-2nd/android-slide/assets/61905052/112389fe-5251-42f4-84ac-6618b84646d8) |
| ---- | ---- | ---- |
