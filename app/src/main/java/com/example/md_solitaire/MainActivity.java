package com.example.md_solitaire;

import android.content.ClipData;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContentInfo;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements HideView.IFlipListener, View.OnTouchListener, View.OnDragListener {

//    private ImageView[] askart = new ImageView[4];

    public static final int STACK_COUNT = 4;
    public static final int DECK_COUNT = 7;

    private final int MAX_LINE = 13;

    private final DeckView deckViews[] = new DeckView[DECK_COUNT];
    private final StackView stackViews[] = new StackView[STACK_COUNT];

    private ShowView showView;
    private HideView hideView;

    private LEVEL level = LEVEL.SINGLE;
//    public Vector<Card> hide = new Vector<>();
//    public Vector<Card> show = new Vector<>();

//
//    private ImageView[][] kapaliKartlar = new ImageView[MAX_ROW][MAX_LINE];
//      private Kart[][] kapaliKartlarBilgi = new Kart[7][19];
//    private ImageView ortakart;
//    private ImageView ortaacikkart;
//    private As as;
//    private Orta orta;
//    private Deste deste = new Deste();
//    private static boolean ortaSuruklendimi = false;
//    private static int kolon_bittimi = 0;


    private void initCards(LEVEL level) {
        Vector<Card> temp = new Vector<>(200);
        if (level == LEVEL.SINGLE) {
            // Step 1 - Toutes les cartes sont instanciées
            for (int i = 1; i <= 13; i++) {
                //黑桃
                temp.add(new Card(Card.CardType.PIQUE, i));
                temp.add(new Card(Card.CardType.PIQUE, i));
                temp.add(new Card(Card.CardType.PIQUE, i));
                temp.add(new Card(Card.CardType.PIQUE, i));
            }
        } else if (level == LEVEL.DOUBLE) {
            // Step 1 - Toutes les cartes sont instanciées
            for (int i = 1; i <= 13; i++) {
                temp.add(new Card(Card.CardType.PIQUE, i));
                temp.add(new Card(Card.CardType.COEUR, i));
                temp.add(new Card(Card.CardType.PIQUE, i));
                temp.add(new Card(Card.CardType.COEUR, i));
            }
        } else {
            // Step 1 - Toutes les cartes sont instanciées
            for (int i = 1; i <= 13; i++) {
                temp.add(new Card(Card.CardType.CARREAU, i));
                temp.add(new Card(Card.CardType.COEUR, i));
                temp.add(new Card(Card.CardType.PIQUE, i));
                temp.add(new Card(Card.CardType.TREFLE, i));
            }
        }

        Vector<Card> hideCards = new Vector<>();
        // Step 2 - On mélange les cartes
        Random random = new SecureRandom();
        while (temp.size() > 0) {
            hideCards.add(temp.remove(random.nextInt(temp.size())));
        }


        // Step 3 - On crée les sept decks avec des cartes tirées aléatoirement dans la pioche
        // Les 7 decks ont des tailles différentes
        for (int deckIndex = 0; deckIndex < DECK_COUNT; deckIndex++) {
            Vector<Card> ss = new Vector<>();
            for (int cardIndex = 0; cardIndex < deckIndex + 1; cardIndex++) {

                ss.add(hideCards.remove(random.nextInt(hideCards.size())));
            }
            deckViews[deckIndex].setCards(ss);
        }

        // Step 4 - On initialise les quatre stacks.
        // On instancie les 4 stacks
        for (int stackIndex = 0; stackIndex < STACK_COUNT; stackIndex++) {
            stackViews[stackIndex].clear();
        }

        showView.clear();
        hideView.setCards(hideCards);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.new_game){
            new AlertDialog.Builder(this).setTitle("Confim!!!").setMessage("Restart a new game!!!").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            }).setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initView() {

        hideView = findViewById(R.id.hide_view);
        hideView.setFlipListener(this);
        showView = findViewById(R.id.show_view);
        showView.setOnTouchListener(this);
        showView.setOnDragListener(this);

        deckViews[0] = findViewById(R.id.deck0);
        deckViews[1] = findViewById(R.id.deck1);
        deckViews[2] = findViewById(R.id.deck2);
        deckViews[3] = findViewById(R.id.deck3);
        deckViews[4] = findViewById(R.id.deck4);
        deckViews[5] = findViewById(R.id.deck5);
        deckViews[6] = findViewById(R.id.deck6);

        stackViews[0] = findViewById(R.id.stack0);
        stackViews[1] = findViewById(R.id.stack1);
        stackViews[2] = findViewById(R.id.stack2);
        stackViews[3] = findViewById(R.id.stack3);

        for (int i = 0; i < STACK_COUNT; ++i) {
            stackViews[i].setOnTouchListener(this);
            stackViews[i].setOnDragListener(this);//new AsDragListener());
//            stackViews[i].setOnReceiveContentListener(new String[]{}, this);
        }
        for (int i = 0; i < DECK_COUNT; ++i) {
            deckViews[i].setOnTouchListener(this);
            deckViews[i].setOnDragListener(this);//new AsDragListener());
        }

    }


    protected void startGame() {
        showView.clear();
        hideView.clear();
        for(int i=0; i<DECK_COUNT; ++i){
            deckViews[i].clear();
        }
        for(int i=0; i<STACK_COUNT; ++i){
            stackViews[i].clear();
        }
        initCards(level);
    }

    protected void stopGame() {
//        game.init();
    }

//
//    private void kartGoster(Kart kart, ImageView kartResim) {
//        AssetManager assets = getAssets();
//        try {
//            InputStream resim = assets.open(kart.toString() + ".png");
//            Drawable flag = Drawable.createFromStream(resim, kart.toString());
//            kartResim.setImageDrawable(flag);
//        } catch (IOException e) {
//        }
//        kartResim.setVisibility(View.VISIBLE);
//    }
//
//    private void turaBasla() {
//        for (int i = 0; i < 7; i++) {
//            Kart kart = deste.randomKartGetir();
//            kartGoster(kart, kapaliKartlar[i][i]);
//            kapaliKartlarBilgi[i][i] = kart;
//            kapaliKartlar[i][i].setOnTouchListener(new MyTouchListener());
//            kapaliKartlar[i][i].setOnDragListener(new MyDragListener());
//            for (int j = i; j < i + 12; j++) {
//                kapaliKartlar[i][j + 1].setVisibility(View.INVISIBLE);
//            }
//        }
//    }
//
//    private void oyunaBasla() {
//        deste = new Deste();
//        as = new As();
//        orta = new Orta(deste);
//        turaBasla();
//    }
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView();

        startGame();
//
//        askart[0] = (ImageView) findViewById(R.id.askart1);
//        askart[1] = (ImageView) findViewById(R.id.askart2);
//        askart[2] = (ImageView) findViewById(R.id.askart3);
//        askart[3] = (ImageView) findViewById(R.id.askart4);
//
//
//        for (int i = 0; i < MAX_ROW; i++) {
//            for (int j = 0; j < MAX_LINE + i; j++) {
//                if (idKartlars[i][j] > 0) {
//                    kapaliKartlar[i][j] = findViewById(idKartlars[i][j]);
//                }
////                String hedef = "kapalikart";
////                hedef = hedef + String.valueOf(i) + "_" + String.valueOf(j + 1);
////                int resID = getResources().getIdentifier(hedef, "id", getPackageName());
////                kapaliKartlar[i][j] = ((ImageView) findViewById(resID));
//            }
//        }
//
//        ortakart = (ImageView) findViewById(R.id.ortakart);
//
//        ortaacikkart = (ImageView) findViewById(R.id.ortaacikkart);
//
//        oyunaBasla();
    }

    @Override
    public void onFinish() {
        hideView.setCards(showView.getCards());
        showView.clear();
    }

    @Override
    public void onFlip(Card card) {
        showView.push(card);
    }

    private boolean isPark(Card now, Card select) {
        if (level == LEVEL.FOUR) {
            if ((now.getType().ordinal() + select.getType().ordinal()) % 2 == 0) {
                return (now.getValue() == (select.getValue() + 1));
            }
        } else {
            if (now.getType() == select.getType()) {
                return (now.getValue() == (select.getValue() + 1));
            }
        }
        return false;
    }

    private boolean checkOver() {
        for (int i = 0; i < DECK_COUNT; ++i) {
            if (!deckViews[i].isEmpty()) {
                return false;
            }
        }
        if (!showView.isEmpty()) {
            return false;
        }
        for (int i = 0; i < STACK_COUNT; ++i) {
            if (stackViews[i].peek().getValue() != 13) {
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean onDrag(View view, DragEvent event) {
        Log.e("wzh", "onDrag:View: " + view + " Evt: " + event);
        if (!(view instanceof DeckView || view instanceof StackView)) {
            return false;
        }

        int action = event.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                ImageView dragObj = (ImageView) event.getLocalState();
                Card select = (Card) dragObj.getTag();
                Holder holder = (Holder) view;
                if (holder.isPark(level, select)) {
                    List<Card> cards = null;
                    if (dragObj.getParent() instanceof Holder) {
                        cards = ((Holder) dragObj.getParent()).pop(dragObj);
                    } else {
                        cards = ((Holder) dragObj).pop(dragObj);
                    }
                    for (Card card : cards) {
                        holder.push(card);
                    }
                    return true;
                }
            case DragEvent.ACTION_DRAG_ENDED:
                if(checkOver()){
                    new AlertDialog.Builder(this).setTitle("Game Over ").setMessage("Restart a new game!!!").setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame();
                        }
                    }).setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).create().show();
                }
//
//                if(dragObj.getParent() instanceof DeckView){
//                    DeckView.pop(dragObj);
//
//                    Card select = (Card) dragObj.getTag();
//                    Holder holder = (Holder) view;
//                    if (holder.isPark(level, select)) {
//
//                    }
//                }else{
//                    Card select = (Card) dragObj.getTag();
//                    Holder holder = (Holder) view;
//                    if (holder.isPark(level, select)) {
//                        holder.push(select);
//                        //
//                        if (dragObj.getParent() instanceof Holder) {
//                            ((Holder) dragObj.getParent()).pop();
//                        }
//                        return true;
//                    }
//                }

                return false;
        }
//
//
//                int asSlot = Integer.parseInt(tag);
//                if (ortaSuruklendimi) {
//                    Log.e("as slot:", tag);
//                    //as slotunda daha oncesinde herhangi kart var mi - yok ise ortadan surukle
//                    if (!as.kartVarmi(asSlot) && orta.getOrtadakiKart().getDeger() == 1) {
//                        kartGoster(orta.getOrtadakiKart(), askart[asSlot - 1]);
//                        //yeni kart bilgisini as slotuna ekliyoruz
//                        as.kartEkle(orta.getOrtadakiKart(), asSlot);
//                        orta.ortadakiKartiSil();
//                        if (orta.getOrtadakiKart() != null) {
//                            kartGoster(orta.getOrtadakiKart(), ortaacikkart);
//                        } else {
//                            ortaacikkart.setImageResource(0);
//                        }
//                    } else if (as.kartVarmi(asSlot)) {
//                        //as slotunda daha oncesinde kart var ise - var ise ortadan surukle
//                        // suruklenen kart view
//                        View view = (View) event.getLocalState();
//                        Kart son_kart = as.ustKartGetir(asSlot);
//                        int eski_deger = orta.getOrtadakiKart().getDeger();
//                        String eski_tip = orta.getOrtadakiKart().getTip();
//                        String eski_renk = orta.getOrtadakiKart().getRenk();
//                        int yeni_deger = son_kart.getDeger();
//                        String yeni_tip = son_kart.getTip();
//                        String yeni_renk = son_kart.getRenk();
//                        //karti koyma sartlari
//                        if (eski_deger == yeni_deger + 1 && eski_deger == yeni_deger + 1 && eski_tip == yeni_tip && eski_renk == yeni_renk) {
//                            kartGoster(orta.getOrtadakiKart(), askart[asSlot - 1]);
//                            //yeni kart bilgisini as slotuna ekliyoruz
//                            as.kartEkle(orta.getOrtadakiKart(), asSlot);
//                            orta.ortadakiKartiSil();
//                            if (orta.getOrtadakiKart() != null) {
//                                kartGoster(orta.getOrtadakiKart(), ortaacikkart);
//                            } else {
//                                ortaacikkart.setImageResource(0);
//                            }
//                        }
//                        ortaSuruklendimi = false;
//                    }
//                } else {
//                    //asagidan asa gelen kartlar
//                    View view = (View) event.getLocalState();
//                    int[] suruklenen = new int[2];
//                    //kart indislerini buluyoruz
//                    for (int i = 0; i < 7; i++) {
//                        for (int j = 0; j < 13 + i; j++) {
//                            if (kapaliKartlar[i][j].getId() == view.getId()) {
//                                suruklenen[0] = i;
//                                suruklenen[1] = j;
//                                break;
//                            }
//                        }
//                    }
//                    int eski_deger = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]].getDeger();
//                    String eski_tip = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]].getTip();
//                    String eski_renk = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]].getRenk();
//                    //as slotunda daha oncesinde herhangi kart var mi - yok ise asagidan surukle
//                    if (!as.kartVarmi(asSlot) && eski_deger == 1) {
//                        kartGoster(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]], askart[asSlot - 1]);
//                        //yeni kart bilgisini as slotuna ekliyoruz
//                        as.kartEkle(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]], asSlot);
//                        kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.INVISIBLE);
//
//                    } else if (as.kartVarmi(asSlot)) {
//                        Kart son_kart = as.ustKartGetir(asSlot);
//                        int yeni_deger = son_kart.getDeger();
//                        String yeni_tip = son_kart.getTip();
//                        String yeni_renk = son_kart.getRenk();
//                        if (eski_deger == yeni_deger + 1 && eski_deger == yeni_deger + 1 && eski_tip == yeni_tip && eski_renk == yeni_renk) {
//                            kartGoster(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]], askart[asSlot - 1]);
//                            //yeni kart bilgisini as slotuna ekliyoruz
//                            as.kartEkle(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]], asSlot);
//                            kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.INVISIBLE);
//
//                        }
//                    }
//                    if (suruklenen[1] > 0) {
//                        //eger suruklenen kartin oncesinde bos bir kart varsa
//                        if (kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].getDrawable().getConstantState() == getResources().getDrawable(R.drawable.bos).getConstantState()) {
//                            if (suruklenen[1] > 0) {
//                                Kart kart = deste.randomKartGetir();
//                                kartGoster(kart, kapaliKartlar[suruklenen[0]][suruklenen[1] - 1]);
//                                kapaliKartlarBilgi[suruklenen[0]][suruklenen[1] - 1] = kart;
//                                kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnTouchListener(new MyTouchListener());
//                                kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnDragListener(new MyDragListener());
//                            }
//                        }
//                    } else if (suruklenen[1] == 0) {
//                        Log.e("kucuk", "o");
//                        kolon_bittimi++;
//                        kapaliKartlar[suruklenen[0]][suruklenen[1]].setImageResource(R.drawable.bos_kart);
//                        kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.VISIBLE);
//                        //kapaliKartlar[suruklenen[0]][suruklenen[1]].setImageResource(0);
//                        kapaliKartlar[suruklenen[0]][suruklenen[1]].setOnDragListener(new MyDragListener());
//                    }
//                }
//                //Oyunun bitme durumu kontrolu
//                int kontrol = 0;
//                for (int i = 0; i < 4; i++) {
//                    if (as.asKartSayisi(i) == 13) {
//                        kontrol++;
//                    } else {
//                        break;
//                    }
//                }
//                if (kontrol == 4) {
//                    Toast.makeText(getApplicationContext(), "Oyun Bitti Tebrikler", Toast.LENGTH_LONG).show();
//                }
//                break;
//            case DragEvent.ACTION_DRAG_ENDED:
//            default:
//                break;
//        }
//        return true;


        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (view.getParent() instanceof DeckView) {
                DeckView deckView = (DeckView) view.getParent();
                if (!deckView.checkDrag(view)) {
                    return false;
                }
                View.DragShadowBuilder shadowBuilder = deckView.getDragView(view);
                ClipData data = ClipData.newPlainText("", "");
                view.startDrag(data, shadowBuilder, view, 0);
            } else {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                //view.setVisibility(View.INVISIBLE);

            }
            return true;
        } else {
            return false;
        }
    }

//    @Nullable
//    @Override
//    public ContentInfo onReceiveContent(@NonNull View view, @NonNull ContentInfo payload) {
//        return null;
//    }

//
//    private final class ortaAcikTouchListener implements View.OnTouchListener {
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                ClipData data = ClipData.newPlainText("", "");
//                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
//                view.startDrag(data, shadowBuilder, view, 0);
//                ortaSuruklendimi = true;
//                return true;
//            } else {
//                return false;
//            }
//        }
//    }
//
//    private final class MyTouchListener implements View.OnTouchListener {
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//
//
//                ClipData data = ClipData.newPlainText("", "");
//                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
//                view.startDrag(data, shadowBuilder, view, 0);
//                //view.setVisibility(View.INVISIBLE);
//                return true;
//            } else {
//                return false;
//            }
//        }
//    }
//
//    class MyDragListener implements View.OnDragListener {
//
//        @Override
//        public boolean onDrag(View v, DragEvent event) {
//            int action = event.getAction();
//            switch (event.getAction()) {
//                case DragEvent.ACTION_DRAG_STARTED:
//                    break;
//                case DragEvent.ACTION_DRAG_ENTERED:
//                    break;
//                case DragEvent.ACTION_DRAG_EXITED:
//                    break;
//                case DragEvent.ACTION_DROP:
//                    Log.e("ortada kalan kart:", String.valueOf(orta.getOrtaKartSayi()));
//                    Log.e("destede kalan kart:", String.valueOf(deste.getDesteKartSayi()));
//                    Log.e("orta suruklendi mi:", String.valueOf(ortaSuruklendimi));
//                    if (ortaSuruklendimi) {
//                        // suruklenen kart view
//                        View view = (View) event.getLocalState();
//                        int[] birakilan = new int[2];
//                        //birakilan kart yeri indisini buluyoruz
//
//                        for (int i = 0; i < 7; i++) {
//                            for (int j = 0; j < 13 + i; j++) {
//                                if (kapaliKartlar[i][j].getId() == v.getId()) {
//                                    birakilan[0] = i;
//                                    birakilan[1] = j;
//                                    break;
//                                }
//                            }
//                        }
//                        int eski_deger = orta.getOrtadakiKart().getDeger();
//                        Log.e("k deger:", String.valueOf(eski_deger));
//                        String eski_tip = orta.getOrtadakiKart().getTip();
//                        String eski_renk = orta.getOrtadakiKart().getRenk();
//                        int yeni_deger = kapaliKartlarBilgi[birakilan[0]][birakilan[1]].getDeger();
//                        String yeni_tip = kapaliKartlarBilgi[birakilan[0]][birakilan[1]].getTip();
//                        String yeni_renk = kapaliKartlarBilgi[birakilan[0]][birakilan[1]].getRenk();
//                        //karti koyma sartlari
//                        if (eski_deger == yeni_deger - 1 && eski_tip != yeni_tip && eski_renk != yeni_renk) {
//                            Log.e("dinlemedi", "hataaaaaa");
//                            //+1 dememizin sebebi bir sonrakine eklemekrtir
//                            kartGoster(orta.getOrtadakiKart(), kapaliKartlar[birakilan[0]][birakilan[1] + 1]);
//                            kapaliKartlar[birakilan[0]][birakilan[1] + 1].setOnTouchListener(new MyTouchListener());
//                            //birakilan eski yere artik kart suruklenemez
//                            kapaliKartlar[birakilan[0]][birakilan[1]].setOnDragListener(null);
//                            //bir sonrakine suruklenebilir
//                            kapaliKartlar[birakilan[0]][birakilan[1] + 1].setOnDragListener(new MyDragListener());
//                            //yeni kart bilgisini guncelliyoruz
//                            kapaliKartlarBilgi[birakilan[0]][birakilan[1] + 1] = orta.getOrtadakiKart();
//                            orta.ortadakiKartiSil();
//                            if (orta.getOrtadakiKart() != null) {
//                                kartGoster(orta.getOrtadakiKart(), ortaacikkart);
//                            } else {
//                                ortaacikkart.setImageResource(0);
//                            }
//                        } else if (kolon_bittimi > 0 && eski_deger == 13) {
//                            // K yi ortadan alip basa ekliyoruz
//                            Log.e("k eklenmeye calisiliyor", "ortadan");
//                            kartGoster(orta.getOrtadakiKart(), kapaliKartlar[birakilan[0]][birakilan[1]]);
//                            kapaliKartlarBilgi[birakilan[0]][birakilan[1]] = orta.getOrtadakiKart();
//                            kapaliKartlar[birakilan[0]][birakilan[1]].setOnTouchListener(new MyTouchListener());
//                            kapaliKartlar[birakilan[0]][birakilan[1]].setOnDragListener(new MyDragListener());
//                            orta.ortadakiKartiSil();
//                            if (orta.getOrtadakiKart() != null) {
//                                kartGoster(orta.getOrtadakiKart(), ortaacikkart);
//                            } else {
//                                ortaacikkart.setImageResource(0);
//                            }
//                            kolon_bittimi--;
//                        }
//                        ortaSuruklendimi = false;
//                    } else {
//                        // suruklenen kart view
//                        View view = (View) event.getLocalState();
//                        int[] suruklenen = new int[2];
//                        int[] birakilan = new int[2];
//                        //kart indislerini buluyoruz
//                        for (int i = 0; i < 7; i++) {
//                            for (int j = 0; j < 13 + i; j++) {
//                                if (kapaliKartlar[i][j].getId() == view.getId()) {
//                                    suruklenen[0] = i;
//                                    suruklenen[1] = j;
//                                    Log.e("deger i:", String.valueOf(i));
//                                    Log.e("deger j:", String.valueOf(j));
//                                    break;
//                                }
//                            }
//                        }
//                        for (int i = 0; i < 7; i++) {
//                            for (int j = 0; j < 13 + i; j++) {
//                                if (kapaliKartlar[i][j].getId() == v.getId()) {
//                                    birakilan[0] = i;
//                                    birakilan[1] = j;
//                                    break;
//                                }
//                            }
//                        }
//                        int eski_deger = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]].getDeger();
//                        String eski_tip = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]].getTip();
//                        String eski_renk = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]].getRenk();
//                        int yeni_deger = kapaliKartlarBilgi[birakilan[0]][birakilan[1]].getDeger();
//                        String yeni_tip = kapaliKartlarBilgi[birakilan[0]][birakilan[1]].getTip();
//                        String yeni_renk = kapaliKartlarBilgi[birakilan[0]][birakilan[1]].getRenk();
//                        //karti koyma sartlari
//                        if (eski_deger == yeni_deger - 1 && eski_tip != yeni_tip && eski_renk != yeni_renk) {
//                            //eger suruklenen kartin pesinde kartlar varsa
//                            Log.e("visible deger:", String.valueOf(kapaliKartlar[suruklenen[0]][suruklenen[1] + 1].getVisibility()));
//                            if (kapaliKartlar[suruklenen[0]][suruklenen[1] + 1].getVisibility() != View.INVISIBLE) {
//                                Log.e("aradan cekildi", "aradannnnnn");
//                                int sayac = 0;
//                                while (kapaliKartlar[suruklenen[0]][suruklenen[1] + sayac].getVisibility() != View.INVISIBLE) {
//                                    //+1 dememizin sebebi bir sonrakine eklemekrtir
//                                    kartGoster(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1] + sayac], kapaliKartlar[birakilan[0]][birakilan[1] + sayac + 1]);
//                                    kapaliKartlar[birakilan[0]][birakilan[1] + sayac + 1].setOnTouchListener(new MyTouchListener());
//                                    //birakilan yere artik kart suruklenemez
//                                    kapaliKartlar[birakilan[0]][birakilan[1] + sayac + 1].setOnDragListener(null);
//                                    //bir sonrakine suruklenebilir
//                                    kapaliKartlar[birakilan[0]][birakilan[1] + sayac + 1].setOnDragListener(new MyDragListener());
//                                    //yeni kart bilgisini guncelliyoruz
//                                    kapaliKartlarBilgi[birakilan[0]][birakilan[1] + sayac + 1] = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1] + sayac];
//                                    kapaliKartlar[suruklenen[0]][suruklenen[1] + sayac].setVisibility(View.INVISIBLE);
//                                    sayac++;
//                                }
//
//
//                            } else {
//                                //+1 dememizin sebebi bir sonrakine eklemekrtir
//                                kartGoster(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]], kapaliKartlar[birakilan[0]][birakilan[1] + 1]);
//                                kapaliKartlar[birakilan[0]][birakilan[1] + 1].setOnTouchListener(new MyTouchListener());
//                                //birakilan yere artik kart suruklenemez
//                                kapaliKartlar[birakilan[0]][birakilan[1]].setOnDragListener(null);
//                                //bir sonrakine suruklenebilir
//                                kapaliKartlar[birakilan[0]][birakilan[1] + 1].setOnDragListener(new MyDragListener());
//                                //yeni kart bilgisini guncelliyoruz
//                                kapaliKartlarBilgi[birakilan[0]][birakilan[1] + 1] = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]];
//                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.INVISIBLE);
//                            }
//                            //alinan kartin yerine yenisini getiriyoruz
//                            //eger kolonun en basinda kart yoksa bos goster
//                            if (suruklenen[1] > 0) {
//                                if (kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].getDrawable().getConstantState() == getResources().getDrawable(R.drawable.bos).getConstantState()) {
//                                    Kart kart = deste.randomKartGetir();
//                                    kartGoster(kart, kapaliKartlar[suruklenen[0]][suruklenen[1] - 1]);
//                                    kapaliKartlarBilgi[suruklenen[0]][suruklenen[1] - 1] = kart;
//                                    Log.e("yeni kart bilgi i:", String.valueOf(suruklenen[0]));
//                                    Log.e("yeni kart bilgi j:", String.valueOf(suruklenen[1] - 1));
//                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnTouchListener(new MyTouchListener());
//                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnDragListener(new MyDragListener());
//                                } else {
//                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnTouchListener(new MyTouchListener());
//                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnDragListener(new MyDragListener());
//                                }
//                            } else if (suruklenen[1] == 0) {
//                                Log.e("kucuk", "o");
//                                kolon_bittimi++;
//                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setImageResource(R.drawable.bos_kart);
//                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.VISIBLE);
//                                //kapaliKartlar[suruklenen[0]][suruklenen[1]].setImageResource(0);
//                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setOnDragListener(new MyDragListener());
//                            }
//                        } else if (kolon_bittimi > 0 && eski_deger == 13) {
//                            // K yi basa ekliyoruz
//                            Log.e("k eklenmeye calisiliyor", "alttan");
//
//                            //eger k nin ardindan kartlar var ise
//                            if (kapaliKartlar[suruklenen[0]][suruklenen[1] + 1].getVisibility() != View.INVISIBLE) {
//                                Log.e("aradan cekildi", "aradannnnnn");
//                                int sayac = 0;
//                                while (kapaliKartlar[suruklenen[0]][suruklenen[1] + sayac].getVisibility() != View.INVISIBLE) {
//                                    //+1 dememizin sebebi bir sonrakine eklemekrtir
//                                    kartGoster(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1] + sayac], kapaliKartlar[birakilan[0]][birakilan[1] + sayac]);
//                                    kapaliKartlar[birakilan[0]][birakilan[1] + sayac].setOnTouchListener(new MyTouchListener());
//                                    //birakilan yere artik kart suruklenemez
//                                    kapaliKartlar[birakilan[0]][birakilan[1] + sayac].setOnDragListener(null);
//                                    //bir sonrakine suruklenebilir
//                                    kapaliKartlar[birakilan[0]][birakilan[1] + sayac].setOnDragListener(new MyDragListener());
//                                    //yeni kart bilgisini guncelliyoruz
//                                    kapaliKartlarBilgi[birakilan[0]][birakilan[1] + sayac] = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1] + sayac];
//                                    kapaliKartlar[suruklenen[0]][suruklenen[1] + sayac].setVisibility(View.INVISIBLE);
//                                    sayac++;
//                                }
//                            } else {
//                                //Sadece K basa ekleniyor
//                                kartGoster(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]], kapaliKartlar[birakilan[0]][birakilan[1]]);
//                                kapaliKartlarBilgi[birakilan[0]][birakilan[1]] = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]];
//                                kapaliKartlar[birakilan[0]][birakilan[1]].setOnTouchListener(new MyTouchListener());
//                                kapaliKartlar[birakilan[0]][birakilan[1]].setOnDragListener(new MyDragListener());
//                                //suruklenen yeri Invisible yapiyoruz
//                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.INVISIBLE);
//                            }
//                            if (suruklenen[1] > 0) {
//                                if (kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].getDrawable().getConstantState() == getResources().getDrawable(R.drawable.bos).getConstantState()) {
//                                    Kart kart = deste.randomKartGetir();
//                                    kartGoster(kart, kapaliKartlar[suruklenen[0]][suruklenen[1] - 1]);
//                                    kapaliKartlarBilgi[suruklenen[0]][suruklenen[1] - 1] = kart;
//                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnTouchListener(new MyTouchListener());
//                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnDragListener(new MyDragListener());
//                                } else {
//                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnTouchListener(new MyTouchListener());
//                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnDragListener(new MyDragListener());
//                                }
//                            } else if (suruklenen[1] == 0) {
//                                Log.e("kucuk", "o");
//                                kolon_bittimi++;
//                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setImageResource(R.drawable.bos_kart);
//                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.VISIBLE);
//                                //kapaliKartlar[suruklenen[0]][suruklenen[1]].setImageResource(0);
//                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setOnDragListener(new MyDragListener());
//                            }
//                            kolon_bittimi--;
//                        }
//                    }
//                    break;
//                case DragEvent.ACTION_DRAG_ENDED:
//                default:
//                    break;
//            }
//            return true;
//        }
//
//    }
//
//    class AsDragListener implements View.OnDragListener {
//
//        @Override
//        public boolean onDrag(View v, DragEvent event) {
//            int action = event.getAction();
//            switch (event.getAction()) {
//                case DragEvent.ACTION_DRAG_STARTED:
//                    break;
//                case DragEvent.ACTION_DRAG_ENTERED:
//                    break;
//                case DragEvent.ACTION_DRAG_EXITED:
//                    break;
//                case DragEvent.ACTION_DROP:
//                    String tag = (String) v.getTag();
//                    int asSlot = Integer.parseInt(tag);
//                    if (ortaSuruklendimi) {
//                        Log.e("as slot:", tag);
//                        //as slotunda daha oncesinde herhangi kart var mi - yok ise ortadan surukle
//                        if (!as.kartVarmi(asSlot) && orta.getOrtadakiKart().getDeger() == 1) {
//                            kartGoster(orta.getOrtadakiKart(), askart[asSlot - 1]);
//                            //yeni kart bilgisini as slotuna ekliyoruz
//                            as.kartEkle(orta.getOrtadakiKart(), asSlot);
//                            orta.ortadakiKartiSil();
//                            if (orta.getOrtadakiKart() != null) {
//                                kartGoster(orta.getOrtadakiKart(), ortaacikkart);
//                            } else {
//                                ortaacikkart.setImageResource(0);
//                            }
//                        } else if (as.kartVarmi(asSlot)) {
//                            //as slotunda daha oncesinde kart var ise - var ise ortadan surukle
//                            // suruklenen kart view
//                            View view = (View) event.getLocalState();
//                            Kart son_kart = as.ustKartGetir(asSlot);
//                            int eski_deger = orta.getOrtadakiKart().getDeger();
//                            String eski_tip = orta.getOrtadakiKart().getTip();
//                            String eski_renk = orta.getOrtadakiKart().getRenk();
//                            int yeni_deger = son_kart.getDeger();
//                            String yeni_tip = son_kart.getTip();
//                            String yeni_renk = son_kart.getRenk();
//                            //karti koyma sartlari
//                            if (eski_deger == yeni_deger + 1 && eski_deger == yeni_deger + 1 && eski_tip == yeni_tip && eski_renk == yeni_renk) {
//                                kartGoster(orta.getOrtadakiKart(), askart[asSlot - 1]);
//                                //yeni kart bilgisini as slotuna ekliyoruz
//                                as.kartEkle(orta.getOrtadakiKart(), asSlot);
//                                orta.ortadakiKartiSil();
//                                if (orta.getOrtadakiKart() != null) {
//                                    kartGoster(orta.getOrtadakiKart(), ortaacikkart);
//                                } else {
//                                    ortaacikkart.setImageResource(0);
//                                }
//                            }
//                            ortaSuruklendimi = false;
//                        }
//                    } else {
//                        //asagidan asa gelen kartlar
//                        View view = (View) event.getLocalState();
//                        int[] suruklenen = new int[2];
//                        //kart indislerini buluyoruz
//                        for (int i = 0; i < 7; i++) {
//                            for (int j = 0; j < 13 + i; j++) {
//                                if (kapaliKartlar[i][j].getId() == view.getId()) {
//                                    suruklenen[0] = i;
//                                    suruklenen[1] = j;
//                                    break;
//                                }
//                            }
//                        }
//                        int eski_deger = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]].getDeger();
//                        String eski_tip = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]].getTip();
//                        String eski_renk = kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]].getRenk();
//                        //as slotunda daha oncesinde herhangi kart var mi - yok ise asagidan surukle
//                        if (!as.kartVarmi(asSlot) && eski_deger == 1) {
//                            kartGoster(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]], askart[asSlot - 1]);
//                            //yeni kart bilgisini as slotuna ekliyoruz
//                            as.kartEkle(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]], asSlot);
//                            kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.INVISIBLE);
//
//                        } else if (as.kartVarmi(asSlot)) {
//                            Kart son_kart = as.ustKartGetir(asSlot);
//                            int yeni_deger = son_kart.getDeger();
//                            String yeni_tip = son_kart.getTip();
//                            String yeni_renk = son_kart.getRenk();
//                            if (eski_deger == yeni_deger + 1 && eski_deger == yeni_deger + 1 && eski_tip == yeni_tip && eski_renk == yeni_renk) {
//                                kartGoster(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]], askart[asSlot - 1]);
//                                //yeni kart bilgisini as slotuna ekliyoruz
//                                as.kartEkle(kapaliKartlarBilgi[suruklenen[0]][suruklenen[1]], asSlot);
//                                kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.INVISIBLE);
//
//                            }
//                        }
//                        if (suruklenen[1] > 0) {
//                            //eger suruklenen kartin oncesinde bos bir kart varsa
//                            if (kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].getDrawable().getConstantState() == getResources().getDrawable(R.drawable.bos).getConstantState()) {
//                                if (suruklenen[1] > 0) {
//                                    Kart kart = deste.randomKartGetir();
//                                    kartGoster(kart, kapaliKartlar[suruklenen[0]][suruklenen[1] - 1]);
//                                    kapaliKartlarBilgi[suruklenen[0]][suruklenen[1] - 1] = kart;
//                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnTouchListener(new MyTouchListener());
//                                    kapaliKartlar[suruklenen[0]][suruklenen[1] - 1].setOnDragListener(new MyDragListener());
//                                }
//                            }
//                        } else if (suruklenen[1] == 0) {
//                            Log.e("kucuk", "o");
//                            kolon_bittimi++;
//                            kapaliKartlar[suruklenen[0]][suruklenen[1]].setImageResource(R.drawable.bos_kart);
//                            kapaliKartlar[suruklenen[0]][suruklenen[1]].setVisibility(View.VISIBLE);
//                            //kapaliKartlar[suruklenen[0]][suruklenen[1]].setImageResource(0);
//                            kapaliKartlar[suruklenen[0]][suruklenen[1]].setOnDragListener(new MyDragListener());
//                        }
//                    }
//                    //Oyunun bitme durumu kontrolu
//                    int kontrol = 0;
//                    for (int i = 0; i < 4; i++) {
//                        if (as.asKartSayisi(i) == 13) {
//                            kontrol++;
//                        } else {
//                            break;
//                        }
//                    }
//                    if (kontrol == 4) {
//                        Toast.makeText(getApplicationContext(), "Oyun Bitti Tebrikler", Toast.LENGTH_LONG).show();
//                    }
//                    break;
//                case DragEvent.ACTION_DRAG_ENDED:
//                default:
//                    break;
//            }
//            return true;
//        }
//
//    }


}
