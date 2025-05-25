package com.manarat.manaratlibrary;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CulturalActivity extends AppCompatActivity {

    private RecyclerView culturalActivitiesRecyclerView;
    private CulturalActivityAdapter adapter;
    private List<CulturalActivityItem> culturalActivitiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultural);

        culturalActivitiesRecyclerView = findViewById(R.id.cultural_activities_recycler_view);
        culturalActivitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // إنشاء قائمة ببيانات الأنشطة الثقافية
        culturalActivitiesList = new ArrayList<>();
        culturalActivitiesList.add(new CulturalActivityItem("ورشة عمل فن الخط", "٢٠٢٥", "رحلة إبداعية من الألف إلى الياء.", R.drawable.calligraphy_workshop_intro_thumb));
        culturalActivitiesList.add(new CulturalActivityItem(" الأدب العربي عبر العصور", "٢٠٢٥", "تطور الأدب العربي عبر العصور الأدبية.", R.drawable.cultural_lecture_arabic_literature, createArabicLiteratureContent()));
        culturalActivitiesList.add(new CulturalActivityItem("مسابقة القصة القصيرة بأقلامهم", "٢٠٢٥", "شاركنا إبداعاتك في كتابة قصص قصيرة.", R.drawable.cultural_short_story_competition));
        culturalActivitiesList.add(new CulturalActivityItem("معرض الاعمال الفنيه", "٢٠٢٥", "عرض أعمال فنية للطلبه والمشاركين.", R.drawable.cultural_art_exhibition));
        // ... إضافة المزيد من الأنشطة هنا

        adapter = new CulturalActivityAdapter(culturalActivitiesList);
        culturalActivitiesRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            CulturalActivityItem clickedItem = culturalActivitiesList.get(position);
            if (clickedItem.getTitle().equals(" الأدب العربي عبر العصور")) {
                Intent intent = new Intent(CulturalActivity.this, ArabicLiteratureDetailsActivity.class);
                intent.putExtra("contentList", (ArrayList<ContentItem>) clickedItem.getContentList());
                startActivity(intent);
            } else if (clickedItem.getTitle().equals("ورشة عمل فن الخط")) {
                Intent intent = new Intent(CulturalActivity.this, CalligraphyWorkshopDetailsActivity.class);
                startActivity(intent);
            } else if (clickedItem.getTitle().equals("مسابقة القصة القصيرة بأقلامهم")) {
                Intent intent = new Intent(CulturalActivity.this, ShortStoryContentActivity.class);
                startActivity(intent);
            } else if (clickedItem.getTitle().equals("معرض الاعمال الفنيه")) {
                Intent intent = new Intent(CulturalActivity.this, ArtExhibitionDetailsActivity.class);
                startActivity(intent);
            }
            // يمكنك إضافة شروط أخرى هنا للانتقال إلى تفاصيل الأنشطة الأخرى
        });
    }

    private List<ContentItem> createArabicLiteratureContent() {
        List<ContentItem> content = new ArrayList<>();
        content.add(new ContentItem(R.drawable.arabic_literature_intro));
        content.add(new ContentItem("إنّ الأدب العربي مر عبر سنين طويلة بمراحل شهدت تطور فنونه أو انحسارها، فلذلك قسم النقاد والدارسون للأدب هذه المراحل إلى عصور أدبية، تقترن بالعصور التاريخية، إذ إنّ العصر الأدبي لا يبدأ ولا ينتهي إلّا عند حدث تاريخي مهم، ومن أوائل من اتبع هذه المنهجية الدكتور شوقي ضيف في سلسلته الشهيرة."));
        content.add(new ContentItem("\n**العصر الجاهلي**"));
        content.add(new ContentItem("يُؤرخ لهذا العصر من الفترة الزمنية المقدرة بـ150 سنة قبل الإسلام، وتُسمى الجاهلية المعروفة، إذ إنّ ما سبق هذه السنوات يُعد مجهولًا، فلم يصلنا عن تلك الفترة أيّ نص أو وثيقة فيما عدا ما ورد في النصوص السماوية، من القرآن كريم وإنجيل وتوراة."));
        content.add(new ContentItem("يُعد هذا العصر عصر نضوج الشعر، إذ يُعتبر أكثر الأجناس الأدبية حضورًا في تلك الفترة، فالشعر ديوان العرب، وهو يُنقل مشافهةً، فلا حاجة للتدوين كباقي الأجناس الأدبية النثرية، فقد كان التدوين شحيحًا بالأصل، فظهرت المعلقات وعيون القصائد العربية، ونضجت قرائح العرب الفنية، فميزوا بين الجيد والرديء، وصار عندهم خلفية ثقافية."));
        content.add(new ContentItem(R.drawable.jahili_poetry));
        content.add(new ContentItem("\n**العصر الإسلامي**"));
        content.add(new ContentItem("أمّا هذا العصر فيبدأ مع بداية البعثة المحمدية الشريفة، فقد كان قدوم الإسلام حدثًا تاريخيًا مهمًا غيّر وجه العالم كافةً، فتم التأريخ به للعصر الأدبي الثاني للعرب، وهو العصر الإسلامي، الذي تغير فيه وجه الأدب لا سيما الشعر، فالعرب كانوا مشغولين بالشعر عن غيره، لكن عندما نزل القرآن تشاغلت به العرب كونه تفوق على الشعر ببلاغته وقوته."));
        content.add(new ContentItem("انقسم الشعر والشعراء إلى فريقين في هذا العصر، ففريق ناصر الدعوة، وفريق كان من مناصري قريش، فكان شعراء قريش يُهاجمون الإسلام ويُسيئون لسيدنا محمد -صلى الله عليه وسلم-، أمّا شعراء الإسلام، فقد جابهوا هذا الهجوم بمدح النبي الكريم، وبالرد على خصومهم، وقد كان أشهر شعراء هذا العصر حسان بن ثابت وكعب بن زهير، وكعب بن مالك وغيرهم."));
        content.add(new ContentItem(R.drawable.islamic_poetry));
        content.add(new ContentItem("\n**العصر الأموي**"));
        content.add(new ContentItem("عدّ الدكتور شوقي ضيف هذا العصر امتدادًا للعصر الإسلامي، إلّا أنّه أفرد له قسمًا كبيرًا من كتابه الموسوم بالعصر الإسلامي، ويُعد هذا العصر فترة إحياء الشعر وانتعاش الأجناس النثرية، فقد استقر العرب في الأمصار، وأخذوا يعودون للشعر تفاخرًا ومدحًا وهجاءً وغزلًا، ومن الشعراء من اتخذ الشعر صنعة يتكسب منها، مثل: الأخطل والفرزدق."));
        content.add(new ContentItem(R.drawable.umayyad_poetry));
        content.add(new ContentItem("انتعشت الأجناس النثرية في هذا العصر، فقد شُيّدت الدولة واستقرّت، وتخلّى العرب عن حياة البداوة وانتشرت الكتابة والتدوين، فظهر فن الرسائل، والخطابة، والوصايا، وغيرها من فنون نثرية تم تدوينها وحفظها، وكان من أعمدة الأدب في ذلك العصر: عبد الحميد الكاتب."));
        content.add(new ContentItem(R.drawable.umayyad_prose));
        content.add(new ContentItem("\n**العصر العباسي**"));
        content.add(new ContentItem("يبدأ التأريخ للعصر العباسي بعد سقوط دمشق حاضرة الأمويين بيد الثورة العباسية، فشكّل هذا العصر ذروة الإبداع العربي في فنون الأدب، ففي الشعر تفرعت موضوعاته، ولم تُعد تقتصر على الرثاء والمدح والغزل، بالإضافة إلى أنّه تم التجديد في هيكل القصيدة وبنائها العام، ونظامها الداخلي وأوزانها الشعرية."));
        content.add(new ContentItem(R.drawable.abbasid_poetry));
        content.add(new ContentItem("أمّا في الجانب النثري، فقد ظهرت المؤلفات الكثيرة في شتى مناحي الأدب، فتم تقعيد اللغة، وظهرت المؤلفات الضخمة، وتم ابتكار أجناس جديد، مثل: فن المقامة، وتم ترجمة كثير من الأعمال الأدبية عن الفارسية والهندية، مثل: ألف ليلة وليلة وحكايات كليلة ودمنة."));
        content.add(new ContentItem(R.drawable.abbasid_prose));
        content.add(new ContentItem("\n**عصر الدول المتتابعة**"));
        content.add(new ContentItem("شهد هذا العصر انهيار الإبداع العربي، إذ شهدت الأمة العربية غزوات كبيرة من الشرق والغرب، فانصرف العرب عن التأليف والكتابة، لكن الشعر بقي حاضرًا في فترة الحكم الأيوبي، إذ اقتصر على رثاء المدن، والتحريض على مواجهة الأعداء، والتغني بالانتصارات."));
        content.add(new ContentItem("لكن ما إن جاء العصرين المملوكي ومن بعده العثماني حتى أصبح الأدب العربي في حالة من التراجع، فلم يعد هنالك نماذج شعرية ونثرية قوية، فانتشر السجع، واستعمال المحسنات اللفظية والمعنوية، وشاع شعر الأحاجي والألغاز."));
        content.add(new ContentItem("\n**العصر الحديث**"));
        content.add(new ContentItem("بدأ هذا العصر بعد دخول نابليون بونابرت لمصر إذ جاء ببعثة علمية، وأحضر معه المطبعة، وبعد ذلك تم تسيير رحلات علمية إلى أوروبا وبدأت جهود المحدثين في تجديد الأدب العربي لا سيما في الشعر، فكان من رواد هذه الفترة: أحمد شوقي ومحمود سامي البارودي."));
        content.add(new ContentItem(R.drawable.modern_arabic_poetry));
        content.add(new ContentItem("أما الفنون النثرية فقد تم تطويرها باستحضار نماذج الأدب الغربي، فتم استحضار القصة القصيرة، وبعد ذلك تم استدراج فن الرواية والمسرحية، وتم التأليف على نمط هذه الفنون، وتم الاتساع فيها وتطويرها بما يناسب البيئة العربية."));
        content.add(new ContentItem(R.drawable.modern_arabic_prose));
        return content;
    }
}