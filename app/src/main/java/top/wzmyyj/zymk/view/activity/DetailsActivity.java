package top.wzmyyj.zymk.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dl7.tag.TagLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.wzmyyj.wzm_sdk.adapter.ViewTitlePagerAdapter;
import top.wzmyyj.wzm_sdk.panel.Panel;
import top.wzmyyj.zymk.R;
import top.wzmyyj.zymk.app.bean.BookBean;
import top.wzmyyj.zymk.app.bean.MuBean;
import top.wzmyyj.zymk.app.bean.XiBean;
import top.wzmyyj.zymk.app.bean.ZiBean;
import top.wzmyyj.zymk.app.tools.G;
import top.wzmyyj.zymk.common.utils.StatusBarUtil;
import top.wzmyyj.zymk.presenter.DetailsPresenter;
import top.wzmyyj.zymk.view.activity.base.BaseActivity;
import top.wzmyyj.zymk.view.adapter.BookAdapter;
import top.wzmyyj.zymk.view.iv.IDetailsView;
import top.wzmyyj.zymk.view.panel.DetailsMuPanel;
import top.wzmyyj.zymk.view.panel.DetailsXiPanel;
import top.wzmyyj.zymk.view.panel.DetailsZiPanel;

public class DetailsActivity extends BaseActivity<DetailsPresenter> implements IDetailsView {


    @Override
    protected void initPresenter() {
        mPresenter = new DetailsPresenter(activity, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_details;
    }


    @Override
    protected void initPanels() {
        super.initPanels();
        addPanels(
                new DetailsXiPanel(context, mPresenter).setTitle("详情"),
                new DetailsMuPanel(context, mPresenter).setTitle("目录"),
                new DetailsZiPanel(context, mPresenter).setTitle("支持")
        );
    }

    private List<BookBean> xgBooks = new ArrayList<>();
    private BookAdapter bookAdapter;

    @Override
    protected void initSome(Bundle savedInstanceState) {
        super.initSome(savedInstanceState);
        StatusBarUtil.initStatusBar(activity, true, true, true);
    }

    // top
    @BindView(R.id.tv_title)
    TextView tv_title;

    //    @BindView(R.id.img_back)
    //    ImageView img_back;
    @OnClick(R.id.img_back)
    void back() {
        mPresenter.finish();
    }

    @BindView(R.id.img_love)
    ImageView img_love;

    // bg
    @BindView(R.id.img_book_bg)
    ImageView img_book_bg;
    @BindView(R.id.tv_book_title)
    TextView tv_book_title;
    @BindView(R.id.tv_book_author)
    TextView tv_book_author;
    @BindView(R.id.tl_book_tag)
    TagLayout tl_book_tag;
    @BindView(R.id.tv_book_ift)
    TextView tv_book_ift;
    @BindView(R.id.img_book)
    ImageView img_book;
    @BindView(R.id.tv_book_star)
    TextView tv_book_star;
    @BindView(R.id.bt_save)
    Button bt_save;
    @BindView(R.id.bt_read)
    Button bt_read;

    // tab
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    // content
    @BindView(R.id.vp_content)
    ViewPager mViewPager;
    @BindView(R.id.rv_books)
    RecyclerView rv_books;


    @Override
    protected void initView() {
        super.initView();


        rv_books.setLayoutManager(new LinearLayoutManager(context, LinearLayout.HORIZONTAL, false));
        bookAdapter = new BookAdapter(context, R.layout.layout_book, xgBooks);
        rv_books.setAdapter(bookAdapter);


        List<View> viewList = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (Panel p : mPanels.getPanelList()) {
            viewList.add(p.getView());
            titles.add(p.getTitle());
        }
        ViewTitlePagerAdapter pagerAdapter = new ViewTitlePagerAdapter(viewList, titles);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);

    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.loadData();
    }

    @Override
    public void update(int w, Object... objs) {

    }

    @Override
    public void setBook(BookBean book) {
        tv_title.setText(book.getTitle());
        tv_book_title.setText(book.getTitle());
        tv_book_author.setText(book.getAuthor());
        tv_book_ift.setText(book.getIft());
        tv_book_star.setText(book.getStar() + "分");

        tl_book_tag.cleanTags();

        if (book.getTags() != null) {
            for (String tag : book.getTags()) {
                tl_book_tag.addTag(tag);
            }
        }
        G.img(context, book.getData_src(), img_book);
        G.imgBlur(context, book.getData_src(), img_book_bg, 20);

    }

    @Override
    public void setXi(XiBean xi) {
        getPanel(0).f(0, xi);
    }

    @Override
    public void setMu(MuBean mu) {
        getPanel(1).f(0, mu);
    }

    @Override
    public void setZi(ZiBean zi) {
        getPanel(2).f(0, zi);
    }


    @Override
    public void setBookList(List<BookBean> list) {
        if (list == null) return;
        xgBooks.clear();
        for (BookBean book : list) {
            xgBooks.add(book);
        }
        bookAdapter.notifyDataSetChanged();
    }
}