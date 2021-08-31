package net.ncplanner.plannerator.planner.menu;
import com.codename1.ui.Button;
import com.codename1.ui.Form;
import com.codename1.ui.TextArea;
import com.codename1.ui.layouts.BorderLayout;
public class MenuCredits extends Form{
    String text = "";
    public MenuCredits(){
        super(new BorderLayout());
        Button b;
        add(TOP, b = new Button("Back"));
        b.addActionListener((evt) -> {
            new MenuMain().showBack();
        });
        TextArea credits = new TextArea();
        credits.setSingleLineTextArea(false);
        credits.setEditable(false);
        add(CENTER, credits);
        credits.getStyle().setAlignment(CENTER);
        credits.getSelectedStyle().setAlignment(CENTER);
        text("NuclearCraft Reactor Generator: Total World Domination");
        text();
        text("Made by ThizThizzyDizzy");
        divider();
        text("Addons");
        text("Tap on any addon name in the addons list to navigate to its CurseForge page");
        text();
        text("QMD");
        text("made by Lach_01298");
        text();
        text("Trinity");
        text("made by Pu-238");
        text();
        text("NCOUTO");
        text("made by Thalzamar and FishingPole");
        text();
        text("Moar Heat Sinks");
        text("made by QuantumTraverse");
        text();
        text("Moar Fuels");
        text("made by QuantumTraverse");
        text();
        text("Moar Reactor Functionality");
        text("made by QuantumTraverse");
        text();
        text("Nuclear Oil Refining");
        text("made by Thalzamar");
        text();
        text("Nuclear Tree Factory");
        text("made by joendter");
        text();
        text("Binary's Extra Stuff");
        text("made by binary_nexus");
        text();
        text("AOP");
        text("made by Thalzamar");
        text();
        text("NCO Confectionery");
        text("made by FishingPole");
        text();
        text("Thorium Mixed Fuels");
        text("made by Thalzamar");
        text();
        text("Inert Matrix Fuels");
        text("made by Cassandra");
        text();
        text("Alloy Heat Sinks");
        text("made by Cn-285");
        text();
        text("Spicy Heat Sinks");
        text("made by Cn-285");
        divider();
        text("Fusion test blanket textures");
        text("by Cn-285");
        divider();
        text("Libraries");
        gap(2);
        text("cn1-filechooser");
        text();
        text("SimpleLibraryPlus");
        text("(a fork of Simplelibrary by computerneek)");
        gap(2);
        text("Made with CodenameOne");
        divider();
        text("Thank you to my patrons for making this possible:");
        text();
        text("General Supporters");
        text("Thalzamar");
        text("Mstk");
        text();
        text("Consider becoming a patron to support further development");
        text("patreon.com/thizthizzydizzy");
        divider();
        text("Thank you to tomdodd4598 for creating such an amazing mod");
        divider();
        text("Thank you to eveyone in the eVault for helping make this planner into what it is");
        divider();
        text();
        credits.setText(text);
    }
    private void text(String str){
        text+=str+"\n";
    }
    private void text(){
        text("");
    }
    private void divider(){
        gap(3);
    }
    private void gap(int num){
        for(int i = 0; i<num; i++)text("");
    }
}