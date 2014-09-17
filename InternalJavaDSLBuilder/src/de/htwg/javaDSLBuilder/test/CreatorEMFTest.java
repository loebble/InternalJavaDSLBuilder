package de.htwg.javaDSLBuilder.test;

import de.htwg.generated.emf.Forum.ForumPackage;
import de.htwg.javaDSLBuilder.creator.CreatorEMF;
import de.htwg.javaDSLBuilder.model.DSLGenerationModel;

public class CreatorEMFTest {

	public static void main(String[] args) {
		ForumPackage forumPackage = ForumPackage.eINSTANCE;
		CreatorEMF creator = CreatorEMF.getInstance(forumPackage);
		DSLGenerationModel builderModel = creator.getGenerationModel();
		System.out.println(builderModel);
		System.out.println();
		System.out.println(builderModel.printOrder());
	}

}
