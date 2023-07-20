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

> ## 3. Unidirectional Data Flow(UDF)
> 230719 PM 2:00
> - SlideManager에 슬라이드 관리 로직을 넣는 2번 단계에서는, UI Layer와 관련된 로직은 모두 MainActivity에서 담당하고 Data Layer와 관련된 일부 로직은 SlideManager에서 담당하였다.
> - 단방향 데이터 흐름을 구현하기 위해 ViewModel이 관리하는 데이터 변경을 수신하고, MainActivity에서 UI를 업데이트하는 구조를 갖추었다.
> - ViewModel에서는 LiveData로 각 속성들을 관리하고 있다. 
> - 배경색이나 알파값 변경등의 명령을 MainActivity에서 ViewModel로 전달하면, 뷰모델은 각 로직을 처리한 후 nowSlide에 변경사항을 적용한 SquareSlide 객체를 갱신한다.
> - MainActivity에서는 ViewModel의 nowSlide를 observe 하고 있다가 값 변경을 감지하면 UI를 업데이트한다.

