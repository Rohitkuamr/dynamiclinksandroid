package com.example.dynamiclinksandroid

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.dynamiclinks.shortLinkAsync
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    var shortLinks= ""
    private lateinit var tex:TextView
lateinit var sharedynamicLinks:Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
  tex=findViewById(R.id.texts)
        val intentFilter=IntentFilter(Intent.ACTION_VIEW)
        intentFilter.addDataScheme("https")
        intentFilter.addDataAuthority("www.google.com",null)
        registerReceiver(dynamicLinkReceiver,intentFilter)

       getshortdynamicslink()
        dynamiclink()
    }
    //implement dynamicLinkReceiver to handle incoming links
    private val  dynamicLinkReceiver = object:BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            TODO("Not yet implemented")
        }

    }
    private fun getshortdynamicslink(){
        Firebase.dynamicLinks.shortLinkAsync {
            link =Uri.parse("https://google.com/?name=prohibit&email=abc@gmail.com")
            domainUriPrefix = "https://dynamiclinksandroid.page.link"

             sharedynamicLinks=findViewById(R.id.dynamiclinkBtn)

            androidParameters { }
            iosParameters("com.ios.com"){}
        }.addOnSuccessListener {
            shortLinks= it.shortLink.toString()
        }.addOnFailureListener {
            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
        }
        sharedynamicLinks.setOnClickListener {
            val intent =Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
//            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT,"Firebase dynamic link")
            intent.putExtra(Intent.EXTRA_TEXT,shortLinks)
            startActivity(Intent.createChooser(intent,"DynamicLink App"))
//            startActivity(intent)
        }
    }
    @SuppressLint("SetTextI18n")
    private fun dynamiclink(){
        Firebase.dynamicLinks.getDynamicLink(intent)
            .addOnSuccessListener {
                var deeplink:Uri?=null
                if (it!=null){
                    deeplink=it.link
                }
                if (deeplink!=null){
                    val name=deeplink.getQueryParameter("name")
                    val email=deeplink.getQueryParameter("email")
                    tex.text="$name  $email"
                }
            }
            .addOnFailureListener {

            }
    }
}