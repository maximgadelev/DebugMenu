package ru.kpfu.itis.crashreporter.crashreporter.src.main.java.com.gadelev.crashreporter.utils;


import androidx.viewpager.widget.ViewPager;


public abstract class SimplePageChangeListener implements ViewPager.OnPageChangeListener {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public abstract void onPageSelected(int position);

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
