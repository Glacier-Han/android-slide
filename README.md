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

> ## 4. 슬라이드 목록 표시
> 230720 AM 10:30
> - RecyclerView에 현재 슬라이드의 목록을 표시하는 기능을 추가하였다.
> - 기존에는 ```SquareSlide``` 객체 기준으로 모든 동작이 이루어졌다. 하지만 새로운 요구사항에 이미지 슬라이드 종류가 새로 추가될 예정이라서 데이터 구조를 변경하였다.
> - ```Slide``` 인터페이스를 만들어서, 슬라이드의 필수적인 요소는 모두 가지고 있게 구현한다. ```slideType, id, side, alpha, selected``` 을 필수 속성으로 간주하였다.
> - ```SquareSlide``` 에는 RGB값을 필수속성으로 추가하였고, ```ImageSlide```에는 image 데이터가 들어갈 변수를 필수속성으로 추가하였다.
> - ```SlideAdapter```에는 ViewType을 분기하여 해당하는 데이터 클래스에서 해당하는 뷰를 띄워주게 구현하였다.
> - ```ItemTouchHelper.Callback()``` 을 사용하여 Drag&Drop으로 순서를 바꿀 수 있게 구현하였다.

> ## 5. 사진 슬라이드 추가하기
> 230721 PM 2:00
> - 슬라이드 목록에서 특정 요소를 길게 터치하면 PopupMenu가 뜬다. 4개의 기능을 활용하여 슬라이드의 순서를 바꿀 수 있다.
> - ```GestureDetector.SimpleOnGestureListener()``` 를 활용하여 더블클릭을 감지 한 후, 이미지 슬라이드일 경우 갤러리를 오픈한다.
> - ```registerForActivityResult```를 활용하여 갤러리에서 선택한 사진을 받아온 후, ```getByteArrayFromUri```를 사용하여 ByteArray로 이미지 정보를 저장한다.
> - 이미지 상단 정렬을 위해서 ConstraintSet에서 제약정보를 동적으로 변경하였다.

> ## 6. 슬라이드(JSON) 불러오기
> 230724 PM 4:00
> - 서버에 저장된 JSON 데이터를 파싱하여 슬라이드로 저장 후 화면에 보여주는 기능 구현
> - 서버와 통신하기 위해 ```Retrofit2``` 라이브러리를 사용하였고, 받아온 JSON을 내가 만든 클래스로 파싱하기 위해 ```Gson``` 라이브러리를 사용하였다.
> - Slide 데이터를 인터페이스로 관리하고 있어서 Gson 기본 ```GsonConverterFactory``` 로는 ```java.lang.UnsupportedOperationException: interface can't be instantiated``` 오류가 발생하였다.
> - 이를 해결하기 위해 ```JsonSlideDeserializer``` 를 만들어서 Slide객체 파싱시 클래스의 분기를 수동으로 설정해주었다.
> - ```Retrofit2``` 통신이 잘 안되어서 여러 방법을 시도해본 결과, 이유는 모르겠지만 한양대Wifi로는 접속이 안되었다. 휴대폰 핫스팟으로 진행하니 잘 되었다.
> - 기존에는 observe중인 데이터가 바뀌면 ```adapter``` 자체를 새로 갱신했는데, 리사이클러뷰에서 ```notifyItemInserted``` 메소드를 실행하는 것으로 변경하니 버벅거림이 개선되었다.

https://github.com/softeerbootcamp-2nd/android-slide/assets/61905052/58317a43-0664-4eed-a81f-d00cc0ac3877

> ## 7. 드로잉 표시하기
> 230727 PM 5:00
> - 드로잉 뷰를 추가하기 위한 ```Model``` 및 ```Custom View``` 구현
> - ```onTouchEvent```로부터 들어오는 ```ACTION_DOWN```/```ACTION_MOVE```/```ACTION_UP``` 트리거를 이용하여 그림 그리기
> - ```ACTION_MOVE```시마다 좌표의 ```min/max```값을 계산하여 그린 그림의 테두리 만들기
> - DataBinding으로 마이그레이션 완료
> - xml에서 처리하기 곤란한 부분은 ```BindingAdapter``` 활용하여 처리하기

> ## 8. 상태 저장하기
> 230730 PM 3:00
> - ```SavedStateHandle``` 를 사용하여 ```ViewModel```의 상태를 저장하기, ```Configuration 변경```에 대응
> - ```SharedPreference```를 활용하여 슬라이드 상태 저장하기. 앱 재실행시에 불러와서 상태 유지하기
> - ```Path()```는 직렬화되지 않기떄문에 ```Point(x: Float, y: Float)``` 객체를 만들어서 관리하기

