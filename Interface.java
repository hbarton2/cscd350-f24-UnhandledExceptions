import java.util.Map;

public class Interface
{
		/*
			ListClasses takes a list of ClassItem, and displays them
		*/
		public void ListClasses(Map<String, ClassItem> classItems)
		{
			for (Map.Entry<String, ClassItem> entry : classItems.entrySet()) 
			{
				System.out.println(entry.getValue().getName);
      }
		}
		
		/*
			ListClass takes a ClassItem reference, and displays its info
		*/
		public void ListClass(ClassItem classItem)
		{
			System.out.println(classItem.toString());
		}
		
		/*
			ListRelationships takes a list of RelationshipItems, and displays them
		*/
		public void ListRelationships(Map<String, RelationshipItem> relationshipItems)
		{
			for (Map.Entry<String, RelationshipItem> entry : relationshipItems.entrySet()) 
			{
				System.out.println(entry.getValue().getName);
      }
		}
		
		/*
			Help displays our help menu and or list of commands
		*/
		public void Help()
		{
			System.out.println("Git Gud");
		}
		
		/*
			Exit says bye and quits
		*/
		public void Exit()
		{
			System.out.println("Bye then.");
			System.exit(0);
		}
};