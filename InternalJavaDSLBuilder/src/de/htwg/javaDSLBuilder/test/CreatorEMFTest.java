package de.htwg.javaDSLBuilder.test;

import de.htwg.generated.emf.ForumMulti.ForumMultiPackage;
import de.htwg.javaDSLBuilder.creator.CreatorEMF;
import de.htwg.javaDSLBuilder.dslmodel.DSLGenerationModel;

public class CreatorEMFTest {

	public static void main(String[] args) {
		ForumMultiPackage forumPackage = ForumMultiPackage.eINSTANCE;
		CreatorEMF creator = CreatorEMF.getInstance(forumPackage);
		DSLGenerationModel builderModel = creator.getGenerationModel();
		System.out.println(builderModel.printModel());
		System.out.println();
		System.out.println(builderModel.printOrder());
	}

}
