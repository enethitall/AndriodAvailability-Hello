package cn.hello.demo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.hello.demo.R

class HelloMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello_main)
    }
}