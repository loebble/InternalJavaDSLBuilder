package de.htwg.javaDSLBuilder.test;

import de.htwg.generated.emf.Forum.ForumPackage;
import de.htwg.generated.regex.User;
import de.htwg.javaDSLBuilder.creator.CreatorEMF;
import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel;

public class CreatorEMFTest {

	public static void main(String[] args) {
		ForumPackage forumPackage = ForumPackage.eINSTANCE;
		CreatorEMF creator = CreatorEMF.getInstance(forumPackage);
		DSLGenerationModel builderModel = creator.getGenerationModel();
		System.out.println(builderModel.printedModel());
		System.out.println();
		System.out.println(builderModel.printedOrder());
	}

}
