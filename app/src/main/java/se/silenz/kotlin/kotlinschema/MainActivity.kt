package se.silenz.kotlin.kotlinschema

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    fun getNovaIDs(){
        var urlcode = ""
        Fuel.post("http://www.novasoftware.se/webviewer/(S(ol3bnszsognoda45gmbo5hba))/MZDesign1.aspx?schoolid=88470&code=151693",
                listOf()).responseString{ request, response, result ->
            run {
                if (response.httpStatusCode==200){
                    val (d, e) = result
                    println("something happens")
                    urlcode = d?.split("WebViewer/")?.get(1)?.split("/printer")!!.get(0)!!
                    Fuel.post("http://www.novasoftware.se/webviewer/"+urlcode+"/MZDesign1.aspx?schoolid=88470&code=151693",
                            listOf("__EVENTTARGET" to "TypeDropDownList", "__EVENTARGUMENT" to "", "__LASTFOCUS" to "", "__VIEWSTATE" to "", "TypeDropDownList" to "1", "ScheduleIDDropDownList" to "0", "FreeTextBox" to "", "PeriodDropDownList" to "8", "WeekDropDownList" to "52", "__VIEWSTATE" to "")).responseString{ request, response, result ->

                        run {
                            val (d, e) = result
                            println(response.url)
                            val nicedata = d?.split("ScheduleIDDropDownList")?.get(2)?.split("<option value=\"");
                            var toTextView = "";
                            for (i in 1..15) {
                                toTextView = toTextView + (nicedata?.get(i)?.split("\"")!!.get(0)) + "\n"
                                toTextView = toTextView + (nicedata?.get(i)?.split(">")!!.get(1).split("<").get(0)) + "\n\n"
                                println(toTextView)
                            }
                            textViewMain.setText(toTextView)

                        }
                    }
                }



            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            run {
                getNovaIDs()
            }
        }
        textViewMain.setMovementMethod(ScrollingMovementMethod());


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
